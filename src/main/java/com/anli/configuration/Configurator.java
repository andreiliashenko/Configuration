package com.anli.configuration;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configurator {

    private static final Logger LOG = LoggerFactory.getLogger(Configurator.class);

    // DEBUG STUB
    public static ConfigSnapshot getConfig(String group) {
        if ("db".equals(group)) {
            return new ConfigSnapshot(group, Collections.singletonMap("connection_pool", "jdbc/BSA"));
        } else {
            LOG.error("Could not resolve configuration group {}", group);
            throw new RuntimeException();
        }
    }
}
