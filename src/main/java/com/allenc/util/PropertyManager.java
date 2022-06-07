package com.allenc.util;

import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Copyright 2022
 */
@Log4j2
public class PropertyManager {

    private static Properties props = new Properties();

    private PropertyManager() {
        // purely a static class
    }

    public static String getProperty(String property) {
        if (props.isEmpty()) {
            log.debug("Property:{} -> not found in the properties file", property);
            return null;
        }
        return props.getProperty(property);
    }

    public static void loadProperties(List<String> propertyFiles) {
        propertyFiles.forEach(propFile -> {
            try {
                props.load(new ClassPathResource(propFile).getInputStream());
            } catch (IOException e) {
                log.debug(e.toString());
                fail("Failed to load " + propFile);
            }
        });
    }

    public static void loadProperties(String... propertyFiles) {
        Arrays.stream(propertyFiles)
              .forEach(propFile -> {
                  try {
                      props.load(new ClassPathResource(propFile).getInputStream());
                  } catch (IOException e) {
                      log.debug(e.toString());
                      fail("Failed to load " + propFile);
                  }
              });
    }

    public static void loadProperty(String key, String value) {
        log.debug("Loading property {} -> {}", key, value);
        props.put(key, value);
    }

    public static void clearProperties() {
        log.debug("Clearing properties");
        props.clear();
    }

}
