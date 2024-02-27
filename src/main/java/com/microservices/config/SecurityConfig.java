package com.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    	
   	 System.out.println("SecurityWebFilterChain");

        http.csrf().disable()
            .authorizeExchange()
               .pathMatchers("/login").permitAll()
               .pathMatchers("/afficher-patients").hasAnyAuthority("ORGANISATEUR", "PRATICIEN")
                .pathMatchers("/afficher-details/**").hasAnyAuthority("ORGANISATEUR", "PRATICIEN")
                .pathMatchers("/modifier-adresse/**", "/modifier-numero/**", "/ajouter-patient").hasAnyAuthority("ORGANISATEUR")
                .pathMatchers("/ajouter-note/**").hasAnyAuthority("PRATICIEN")
                .pathMatchers("/**").authenticated()
                .anyExchange().authenticated()
                .and()
            .httpBasic()
                .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));

        return http.build();
    }
   
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
    	
      	 System.out.println("MapReactiveUserDetailsService");
    	
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails org = User.withUsername("org")
                .password(encoder.encode("org"))
                .roles("ORGANISATEUR")
                .build();
        UserDetails pra = User.withUsername("pra")
                .password(encoder.encode("pra"))
                .roles("PRATICIEN")
                .build();
        return new MapReactiveUserDetailsService(org, pra);
    }   
}