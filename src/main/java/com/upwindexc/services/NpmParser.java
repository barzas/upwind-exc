package com.upwindexc.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwindexc.models.EcoSysPackage;

public class NpmParser implements IEcoSystemParser {
    private String dependeciesReg = "dependencies";
    private String devDependeciesReg = "devDependencies";
    private List<EcoSysPackage> dependencies = new ArrayList<>();

    @Override
    public List<EcoSysPackage> parse(String fileContent) {
        ObjectMapper mapper = new ObjectMapper();
        
        fileContent = this.decode(fileContent);

        try {
            JsonNode rootNode = mapper.readTree(fileContent);
            extractDependencyNames(rootNode, dependeciesReg);
            extractDependencyNames(rootNode, devDependeciesReg);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return dependencies;
    }

    private void extractDependencyNames(JsonNode rootNode, String section) {
        JsonNode node = rootNode.get(section);
        if (node != null && node.isObject()) {
            node.fieldNames().forEachRemaining(depName -> {
                
                EcoSysPackage pkg = new EcoSysPackage();
                pkg.setName(depName);
                pkg.setVersion(node.get(depName).asText());
                
                dependencies.add(pkg);
            });
        }
    }
    
}
