package com.example.securingweb.utils;


import com.example.securingweb.bean.AllianceContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class CustomJWTConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Value("${keycloak.client.id}")
    private String keyclockClientId;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, roles);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if (jwt.getClaim("resource_access") != null) {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

            List<String> userGroup = jwt.getClaim("user_group");
            AllianceContext.setCurrentAlliance(userGroup);

            System.out.println(resourceAccess);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> rolesMap = (Map<String, Object>) resourceAccess.get(keyclockClientId);
            List<String> keycloakRoles = mapper.convertValue(rolesMap.get("roles"), new TypeReference<List<String>>() {
            });
            System.out.println(keycloakRoles);
            List<GrantedAuthority> roles = new ArrayList<>();

            for (String keycloakRole : keycloakRoles) {
                roles.add(new SimpleGrantedAuthority(keycloakRole));
            }


            return roles;
        }
        return new ArrayList<>();
    }


}
