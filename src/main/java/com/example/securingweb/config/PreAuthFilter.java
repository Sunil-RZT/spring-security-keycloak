
package com.example.securingweb.config;


import com.example.securingweb.bean.AllianceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PreAuthFilter extends OncePerRequestFilter
{
    static String SUPER_ADMIN_ROLE = "superadmin";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Auth Filter applied for
        System.out.println("Auth Filter applied for " + request.getRequestURL().toString());
        // Access headers from the request
        String jwtToken = request.getHeader("Authorization");
        String[] chunks = jwtToken.replace("Bearer ", "").split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println(header + "\n");
        System.out.println(payload);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonObject = objectMapper.readValue(payload, Map.class);

        if ( jsonObject.containsKey("roles"))
        {
            List<String> userRoles = (List<String>) jsonObject.get("roles");
            if (jsonObject.containsKey("user_group"))
            {
                AllianceContext.setCurrentAlliance((List<String>) jsonObject.get("user_group"));
            }
            else if (userRoles.contains(SUPER_ADMIN_ROLE))
            {
                // User is Admin - need not to be associated in any group, has access to all Alliance Data
                AllianceContext.setCurrentAlliance(Collections.singletonList("PERMIT_ALL"));
            }


        }
        else {
            System.out.println("WARN : NO Role assigned to the user : " + jsonObject.get("preferred_username"));
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        // Exclude certain URLs from being processed by this filter
        String path = request.getRequestURI();
        System.out.println("Skipped Auth Filter for : " + path);
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

}
