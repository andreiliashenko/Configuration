package com.anli.configuration;

import java.util.Collections;

public class Configurator {
    // DEBUG STUB
    public static ConfigSnapshot getConfig(String group) {
        if ("db".equals(group)) {
            return new ConfigSnapshot(group, Collections.singletonMap("connection_pool", "jdbc/BSA"));
        } else {
            throw new RuntimeException();
        }      
    }
}
