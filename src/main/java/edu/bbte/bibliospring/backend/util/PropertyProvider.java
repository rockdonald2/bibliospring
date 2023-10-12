package edu.bbte.bibliospring.backend.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class PropertyProvider {

    private static final Properties properties;

    static {
       try (final InputStream propertiesStream =  PropertyProvider.class.getClassLoader().getResourceAsStream("application.properties")) {
           properties = new Properties();
           properties.load(propertiesStream);
       } catch (IOException e) {
           throw new IllegalStateException("Failed to load application properties");
       }
    }

    private PropertyProvider() {

    }

    public static synchronized Optional<String> getProperty(String propertyName) {
        return Optional.ofNullable(properties.getProperty(propertyName));
    }

}
