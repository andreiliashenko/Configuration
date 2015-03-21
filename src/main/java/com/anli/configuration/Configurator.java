package com.anli.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configurator {

    private static final Logger LOG = LoggerFactory.getLogger(Configurator.class);

    private static final String GLASSFISH_INSTANCE_ROOT_PROPERTY = "com.sun.aas.instanceRoot";
    private static final String GLASSFISH_CONFIG_FOLDER = "config";
    private static final String GLASSFISH_TRANSACTION_MANAGER_JNDI_NAME =
            "java:appserver/TransactionManager";

    private static final String CDI_BEAN_MANAGER_JNDI_NAME = "java:comp/BeanManager";

    private static final String MAIN_CFG_FILE = "main.cfg";

    private static volatile Map<String, ConfigSnapshot> snapshots = null;

    private static synchronized void loadConfiguration() {
        if (snapshots != null) {
            return;
        }
        Map<String, ConfigSnapshot> formedSnapshots = new HashMap<>();
        String rootDirectoryPath = getRootDirectory();
        LOG.info("Root directory path: {}", rootDirectoryPath);
        File rootDirectory = new File(rootDirectoryPath);
        Map<String, String> snapshotPaths = readProperties(rootDirectory, MAIN_CFG_FILE);
        for (Map.Entry<String, String> path : snapshotPaths.entrySet()) {
            Map<String, String> snapshotProperties = readProperties(rootDirectory, path.getValue());
            String group = path.getKey();
            ConfigSnapshot snapshot = new ConfigSnapshot(group, snapshotProperties);
            formedSnapshots.put(group, snapshot);
        }
        snapshots = formedSnapshots;
    }

    private static String getRootDirectory() {
        // TO DO: implement another app servers implementations
        LOG.info("Defining root directory for GlassFish Application Server...");
        return System.getProperty(GLASSFISH_INSTANCE_ROOT_PROPERTY)
                + File.separator + GLASSFISH_CONFIG_FOLDER;
    }

    private static Map<String, String> readProperties(File rootDirectory, String filePath) {
        File propertyFile = new File(rootDirectory, filePath);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertyFile));
        } catch (IOException ioException) {
            LOG.error("Could not load config file " + propertyFile.getAbsolutePath(), ioException);
        }
        HashMap<String, String> propertyMap = new HashMap<>();
        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            propertyMap.put(property.getKey().toString(), property.getValue().toString());
        }
        LOG.info("{} read contents: {}", propertyFile.getAbsolutePath(), propertyMap);
        return propertyMap;
    }

    public static ConfigSnapshot getConfig(String group) {
        if (snapshots == null) {
            loadConfiguration();
        }
        return snapshots.get(group);
    }

    public static String getTransationManagerJndiName() {
        // TODO: implement another app servers implementations
        return GLASSFISH_TRANSACTION_MANAGER_JNDI_NAME;
    }

    public static String getCdiBeanManagerJndiName() {
        return CDI_BEAN_MANAGER_JNDI_NAME;
    }
}
