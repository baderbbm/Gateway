package com.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf().disable().authorizeExchange()
		        .pathMatchers(HttpMethod.POST, "/patients/**").hasAnyAuthority("ROLE_ORGANISATEUR")
				.pathMatchers(HttpMethod.GET,"/patients/**").hasAnyAuthority("ROLE_ORGANISATEUR", "ROLE_PRATICIEN", "ROLE_TECHNIQUE")
				.pathMatchers("/medecin/notes/**").hasAnyAuthority("ROLE_ORGANISATEUR", "ROLE_PRATICIEN", "ROLE_TECHNIQUE")			
				.pathMatchers("/**").authenticated().and().httpBasic()
				.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));

		return http.build();
	}

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserDetails org = User.withUsername("org").password(encoder.encode("org")).roles("ORGANISATEUR").build();
		UserDetails pra = User.withUsername("pra").password(encoder.encode("pra")).roles("PRATICIEN").build();
		UserDetails tech = User.withUsername("diabete").password(encoder.encode("diabete")).roles("TECHNIQUE").build();
		return new MapReactiveUserDetailsService(org, pra, tech);
	}
	
	@Bean
    public RolesExtractionFilter rolesExtractionFilter() {
        return new RolesExtractionFilter();
    }
}
