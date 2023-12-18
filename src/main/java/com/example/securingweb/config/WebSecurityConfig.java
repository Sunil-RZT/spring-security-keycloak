package com.example.securingweb.config;


import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;


@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disable Cross Site Reference Forgery
        http.csrf(AbstractHttpConfigurer::disable);

        /*
        Key cloak Authentication
         */
        http.addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class)
                .addFilterAfter(new PreAuthFilter(), BearerTokenAuthenticationFilter.class);

        // user_id , group_name ( Header )

        // API to get status of some job id ( App BE --- > Master BE )
        // API to start a job

        http.sessionManagement(sessionManagement -> {
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        return http.build();


    }

    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
        return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {

            @Override
            public PolicyEnforcerConfig resolve(HttpRequest request) {
                try {
                    return JsonSerialization.
                            readValue(getClass().getResourceAsStream("/policy-enforcer.json"),
                                    PolicyEnforcerConfig.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}

