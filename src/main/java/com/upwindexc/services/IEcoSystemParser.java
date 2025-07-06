package com.upwindexc.services;

import java.util.Base64;
import java.util.List;

import com.upwindexc.models.EcoSysPackage;


public interface IEcoSystemParser {
    
    /**
     * Decodes a Base64 encoded string and returns the JSON content.
     *
     * @param fileContent the Base64 encoded string
     * @return the decoded JSON content as a String
     */
    default String decode(String fileContent) {
        byte[] decodedBytes = Base64.getDecoder().decode(fileContent);
        String jsonContent = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
        return jsonContent; 
    };

    public abstract List<EcoSysPackage> parse(String fileContent);
}
