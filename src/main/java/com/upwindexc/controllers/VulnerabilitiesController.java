package com.upwindexc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upwindexc.models.EEcoSystemType;
import com.upwindexc.models.EcoSysPackage;
import com.upwindexc.models.ScanRequest;
import com.upwindexc.models.VulnerabilityResponse;
import com.upwindexc.providers.GithubProvider;
import com.upwindexc.services.IEcoSystemParser;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vulnerabilities")
public class VulnerabilitiesController {

    @PostMapping("/scan")
    public ResponseEntity<?> scanVulnerabilities(@Valid @RequestBody ScanRequest scanRequest) {        
        IEcoSystemParser parser;
        boolean isEcoSystemSupported = false;
        List<EcoSysPackage> packages = new ArrayList<>();
        GithubProvider provider = new GithubProvider();

        scanRequest.setEcosystem(scanRequest.getEcosystem().toUpperCase());

        for (EEcoSystemType ecosystemType : EEcoSystemType.values()) {
            if (ecosystemType.name().equals(scanRequest.getEcosystem())) {
                
                parser = ecosystemType.create();
                packages = parser.parse(scanRequest.getFileContent());
                isEcoSystemSupported = true;
                break;
            }
        }

        if (!isEcoSystemSupported) {
            return ResponseEntity.badRequest().body("Unsupported ecosystem type: " + scanRequest.getEcosystem());
        } else if (packages.isEmpty()) {
            return ResponseEntity.badRequest().body("No packages found in the provided file content.");
        }
        
        VulnerabilityResponse responses = new VulnerabilityResponse();
        
        for (int i=0; i < packages.size(); i++) {
            try {
                responses.getVulnerabilities().addAll(provider.checkVulnerabilities(scanRequest.getEcosystem(), packages.get(i)));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Something get wrong while askingthe provider");
            }
        }

        return ResponseEntity.ok().body(responses);
    } 
}
