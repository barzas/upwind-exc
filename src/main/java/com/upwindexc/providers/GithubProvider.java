package com.upwindexc.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.upwindexc.models.EcoSysPackage;
import com.upwindexc.models.VulnerabilityResponse;
import com.vdurmont.semver4j.Semver;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GithubProvider extends GraphQlProvider {

    public GithubProvider() {
        super();
        super.setApiUrl(System.getenv("GITHUB_GRAPHQL_API"));
        super.setToken(System.getenv("GITHUB_TOKEN"));
    }
    
    private final String VulnerabilitiesQuery = """
            query($ecosystem: SecurityAdvisoryEcosystem!, $packageName: String!) {
              securityVulnerabilities(ecosystem: $ecosystem, first: 100, package: $packageName) {
                nodes {
                  severity
                  package {
                    name
                    ecosystem
                  }
                  vulnerableVersionRange
                  firstPatchedVersion {
                    identifier
                  }
                }
              }
            }
            """;

    @Override
    public List<VulnerabilityResponse.Vulnerability> checkVulnerabilities(String ecosystem, EcoSysPackage pack) throws Exception {
        ResponseEntity<String> response = super.executeGraphQl(ecosystem, pack, VulnerabilitiesQuery);

        // convert response to JsonNode
        JsonNode root = super.getObjectMapper().readTree(response.getBody());
        JsonNode nodes = root.path("data").path("securityVulnerabilities").path("nodes");

        VulnerabilityResponse filteredResponse = filterIRelevantResponseAccordingNPMVersions(nodes, ecosystem, pack);
        
        return filteredResponse.getVulnerabilities();
        
    }

    
    private VulnerabilityResponse filterIRelevantResponseAccordingNPMVersions(JsonNode nodes, String ecosystem, EcoSysPackage pack) {
       
      Semver mySemver = new Semver(pack.getVersion(), Semver.SemverType.NPM);

        VulnerabilityResponse result = new VulnerabilityResponse();
        if (nodes.isArray()) {
            for (JsonNode node : nodes) {

                String versionRange = node.path("vulnerableVersionRange").asText();
                versionRange = versionRange.replace(',', ' ');
                if (versionRange == null || versionRange.isBlank() || !mySemver.satisfies(versionRange)) {
                    continue;
                }

                VulnerabilityResponse.Vulnerability v = new VulnerabilityResponse.Vulnerability();
                v.setSeverity(node.path("severity").asText());
                v.setPackageName(node.path("package").path("name").asText());
                v.setVersion(pack.getVersion());
                v.setFirstPatchedVersion(node.path("firstPatchedVersion").path("identifier").asText());

                result.addVulnerabilities(v);
            }
        }
 
        return result;
    }
    
}
