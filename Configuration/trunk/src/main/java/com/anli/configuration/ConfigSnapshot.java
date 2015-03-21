package com.anli.configuration;

import java.util.Map;

public class ConfigSnapshot {

    protected String group;
    protected Map<String, String> properties;

    public ConfigSnapshot(String group, Map<String, String> properties) {
        this.properties = properties;
    }

    public String getProperty(String id) {
        return properties.get(id);
    }

    public String getGroup() {
        return group;
    }
}
