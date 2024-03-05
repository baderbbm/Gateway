package com.microservices.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class RolesExtractionFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
                    String roles = userDetails.getAuthorities().toString(); // Récupérer les rôles de l'utilisateur
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Roles", roles)
                            .build();     
                    System.out.println(request.getHeaders());
                    return exchange.mutate().request(request).build();
                })
                .then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
