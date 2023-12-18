package com.example.securingweb.controller;

import com.example.securingweb.bean.AllianceContext;
import com.example.securingweb.bean.DocRegistry;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

//@Tag(name = "SinglePointController", description = "APIs")
@RestController
//@RequestMapping("/app")
@SecurityRequirement(name="Keycloak")
public class SinglePointController {
    @Autowired
    DocRegistry docRegistry;

    @GetMapping
    @RequestMapping("/public/welcome")
    // Public API
    public String getWelcomeMessage() {
        return "Welcome to SinglePoint AI Solution";
    }

    @GetMapping
    @RequestMapping("/api/operations")
    // Any logged-in user
    public List<String> listOperations() {
        System.out.println("Current Alliance : "+  AllianceContext.getCurrentAlliance());
        return Arrays.asList("Ingestion", "Extraction", "Validation", "Active Learning Status");
    }

    @GetMapping
    @RequestMapping("/api/docs/all")
    // Only logged-in user with role SuperAdmin
    public List<String> listAllDocuments() {
        System.out.println("Current Alliance : "+  AllianceContext.getCurrentAlliance());
        return Arrays.asList("PDQ.pdf", "PAF.pdf", "SPD.pdf", "PD.pdf");
    }

    @GetMapping
    @RequestMapping("/api/docs/alliance")
    // Only logged-in user belongs to particular alliance group
    public List<String> listAllianceDocuments()
    {
        System.out.println("Current Alliance : "+  AllianceContext.getCurrentAlliance());
        return docRegistry.getDocuments();
    }
}
