package com.example.securingweb;

import com.example.securingweb.bean.AllianceContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class JWTParserTest
{
    static String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFTWpfa3FEak1GNnVDTnZILVdpX2lLTTlJTEhYRWpDajEzTFZWMUxscnhBIn0.eyJleHAiOjE3MDI4MzMxNTgsImlhdCI6MTcwMjgzMjg1OCwianRpIjoiYjIyMzg4MjMtMWMzNS00NzcxLWJkZjAtYWMxZmUwMTEwNjJhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL3JlYWxtcy9zaW5nbGVwb2ludC1haSIsInN1YiI6ImQyNmNkZmVmLTUxZjEtNDJiZS05YmIxLTExNmE0MTIyYTRlMCIsInR5cCI6IkJlYXJlciIsImF6cCI6InNpbmdsZXBvaW50LWFpLWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiJhNzgyM2E0Zi0wZDViLTQxNjEtYTA1OS1kMjZjOTMyZjQxODQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODg4OC8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJhbGxpYW5jZS11c2VyIl19LCJzY29wZSI6ImFsbGlhbmNlX25hbWUgZW1haWwgcHJvZmlsZSIsInNpZCI6ImE3ODIzYTRmLTBkNWItNDE2MS1hMDU5LWQyNmM5MzJmNDE4NCIsInVzZXJfZ3JvdXAiOlsiL0FtZXJpY2FuIEZ1bmRzIl0sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicm9sZXMiOlsiYWxsaWFuY2UtdXNlciJdLCJuYW1lIjoiYWYgdXNlciIsImdyb3VwcyI6WyJhbGxpYW5jZS11c2VyIl0sInByZWZlcnJlZF91c2VybmFtZSI6ImFmX3VzZXIiLCJnaXZlbl9uYW1lIjoiYWYiLCJmYW1pbHlfbmFtZSI6InVzZXIiLCJlbWFpbCI6InVzZXJAYW1lcmljYW5mdW5kcy5jb20ifQ.X1z4PyPXsDXj7SUz-zgPAtf8PrB_5-SIL0ubnoFNM6ylGeNaRsWjNWO7od4iujbS0pxMAEz_LBZo7IkW4eZypjGh8udBckU8eAb0mf4arL-5VhtNExBEMrMwe-pSz2uilgvLXFSzzSryPSU2G0Wwd3bGDb5nH8y4L_kYsLjtBPcjGxv8GtQ1afKVR_TY4NKb1ToeVxabMuTusEC8RgxByr-zxfRrZ5qgEI8x7CnKWWE-2EJgnl6A3_d6X2l9fc84zVfQQoWxgdRydDa0F4Z49xiQAR9wPSB_g_gPXzFRbCZp_-L_1RfFWV1ZqCj-zP39NeWWGnLR86jm80dxpda5bg";
    static String SUPER_ADMIN_ROLE = "superadmin";
    @Test
    void contextLoads() throws JsonProcessingException {
        if (jwtToken != null) {
            String[] chunks = jwtToken.split("\\.");
            System.out.println(chunks + "\n");

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



            // Extract information from the claims
//            String username = claims.getSubject();
//            String userGroup = (String) claims.get("user_group");

//            System.out.println("Username: " + username);
//            System.out.println("Groups: " + userGroup);
            System.out.println("Groups: " + jsonObject.toString());
        }
    }

}
