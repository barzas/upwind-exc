package com.upwindexc.models;

import jakarta.validation.constraints.NotNull;

public class ScanRequest {
    
    /*
     * Name of the ecosystem the project file is using - e.g. npm, nuget, pip.
        Note: for this exercise, only npm is used
     */
    @NotNull
    private String ecosystem;

    /*
     * The project definition file content,
        encoded in base64.
     */
    @NotNull
    private String fileContent;

    public String getEcosystem() {
        return ecosystem;
    }

    public void setEcosystem(String ecosystem) {
        this.ecosystem = ecosystem;
    }

    public String getFileContent() {
        return fileContent;
    }

    
}
