package com.upwindexc.providers;

import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.springframework.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwindexc.models.EcoSysPackage;
import com.upwindexc.models.VulnerabilityResponse;

abstract class GraphQlProvider {
    
    private String apiUrl;
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setApiUrl(String apiUrl) {this.apiUrl = apiUrl; }

    public void setToken(String token) {this.token = token; }

    public ObjectMapper getObjectMapper() {return objectMapper; }   

    public abstract List<VulnerabilityResponse.Vulnerability> checkVulnerabilities(String ecosystem, EcoSysPackage pack) throws Exception;

    public ResponseEntity<String> executeGraphQl(String ecosystem, EcoSysPackage pack, String query) throws Exception {

        // Build JSON payload
        String payload = objectMapper.writeValueAsString(
            new GraphQLRequest(query, ecosystem, pack.getName())
        );

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.token);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange(
                this.apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    static class GraphQLRequest {
        private String query;
        private Variables variables;

        GraphQLRequest(String query, String ecosystem, String packageName) {
            this.query = query;
            this.variables = new Variables(ecosystem, packageName);
        }

        static class Variables {
            private String ecosystem;
            private String packageName;

            Variables(String ecosystem, String packageName) {
                this.ecosystem = ecosystem;
                this.packageName = packageName;
            }

            public String getEcosystem() {return ecosystem; }

            public String getPackageName() {return packageName; }
        }

        // getters
        public String getQuery() { return query; }
        public Variables getVariables() { return variables; }
    }
}
