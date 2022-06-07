package com.allenc;

import org.junit.jupiter.api.BeforeEach;

import static com.allenc.util.PropertyManager.loadProperties;

/**
 * @author Copyright 2022
 */
public class TestBase extends CommonsTestBase {


    @BeforeEach
    void commonsBaseSetup() {
        loadProperties("browser.properties");
    }

}
