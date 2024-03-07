package com.microservices.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//filtre global pour toutes les requêtes passant par la gateway
@Component
public class RolesExtractionFilter implements GlobalFilter, Ordered {

	@Override
	// méthode appelée pour chaque requête entrante
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// récupère le contexte de sécurité réactif
		return ReactiveSecurityContextHolder.getContext().map(securityContext -> {
			// récupérer l'entité représentant l'utilisateur authentifié
			UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
			// récupérer les rôles de l'utilisateur
			String roles = userDetails.getAuthorities().toString();
			// ajouter les rôles à l'en-tête de la réponse
			exchange.getResponse().getHeaders().add("X-User-Roles", roles);
			return exchange;
		})
			// appelle la chaîne de filtres suivante dans le pipeline de filtrage
			.flatMap(chain::filter);
	}

	// la méthode spécifie l'ordre dans lequel ce filtre doit être exécuté parmi d'autres filtres globaux
	// -1 signifie qu'il est exécuté avant les autres filtres globaux
	@Override
	public int getOrder() {
		return -1;
	}
}
