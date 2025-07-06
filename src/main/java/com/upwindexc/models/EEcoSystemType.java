package com.upwindexc.models;

import java.util.function.Supplier;

import com.upwindexc.services.IEcoSystemParser;
import com.upwindexc.services.NpmParser;

public enum EEcoSystemType {
    
    NPM(NpmParser::new);

    private final Supplier<IEcoSystemParser> supplier;

    EEcoSystemType(Supplier<IEcoSystemParser> supplier) {
        this.supplier = supplier;
    }

    public IEcoSystemParser create() {
        return supplier.get();
    }
}
