package com.allenc.util;

import static org.assertj.core.api.Assertions.assertThat;

import static com.allenc.util.PropertyManager.clearProperties;
import static com.allenc.util.PropertyManager.getProperty;
import static com.allenc.util.PropertyManager.loadProperties;
import static com.allenc.util.PropertyManager.loadProperty;

import com.allenc.TestBase;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * @author Copyright 2022
 */
class PropertyManagerTest extends TestBase {

    @Test
    void loadListOfProperties() {
        loadProperties(Arrays.asList("test.properties", "test2.properties"));
        assertThat(getProperty("testProperty")).isEqualTo("thisIsATestProperty");
        assertThat(getProperty("anotherTestProperty")).isEqualTo("thisIsAnotherTestProperty");
    }

    @Test
    void loadVarargsOfProperties() {
        loadProperties("test.properties", "test2.properties");
        assertThat(getProperty("testProperty")).isEqualTo("thisIsATestProperty");
        assertThat(getProperty("anotherTestProperty")).isEqualTo("thisIsAnotherTestProperty");
    }

    @Test
    void getPropertyInvalidProperty() {
        loadProperties("test.properties", "test2.properties");
        assertThat(getProperty("testProperty")).isEqualTo("thisIsATestProperty");
        assertThat(getProperty("invalidProperty")).isNull();
    }

    @Test
    void loadPropertyLoadsAProperty() {
        loadProperty("testPropertyToLoad", "loadedTestProperty");
        assertThat(getProperty("testPropertyToLoad")).isEqualTo("loadedTestProperty");
    }

    @Test
    void clearPropertiesClears() {
        loadProperties("test.properties", "test2.properties");
        assertThat(getProperty("testProperty")).isEqualTo("thisIsATestProperty");
        clearProperties();
        assertThat(getProperty("testProperty")).isNull();
    }

}
