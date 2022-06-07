package com.allenc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;

/**
 * @author Copyright 2022
 */
@Log4j2
public class JsonUtil {

    private static final ObjectMapper JSON_PRETTY_MAPPER = new ObjectMapper();
    private static final ObjectMapper JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER = new ObjectMapper();
    private static final HashMap mapperMap = new HashMap();

    private JsonUtil() {}

    private static void addMapper(Module module, LeniencyOption... options) {
        Set<LeniencyOption> optionSet = new HashSet(Arrays.asList(options));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        if (!optionSet.contains(LeniencyOption.ALLOW_DUPS)) {
            mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
            mapper.configure(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION, true);
        }
        if (optionSet.contains(LeniencyOption.ALLOW_UNKNOWN)) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        mapper.findAndRegisterModules();
        mapper.registerModule(module);
        mapperMap.put(optionSet, mapper);
    }

    public static ObjectMapper getMapper(LeniencyOption... options) {
        Set<LeniencyOption> optionSet = new HashSet(Arrays.asList(options));
        Iterator var2 = mapperMap.entrySet().iterator();
        Map.Entry entry;
        do {
            if (!var2.hasNext()) {
                throw new IllegalArgumentException("No object mapper defined with options of [" + options + "]");
            }
            entry = (Map.Entry)var2.next();
        } while (!optionSet.equals(entry.getKey()));

        return (ObjectMapper)entry.getValue();
    }

    public static JsonNode parseJson(String inputString, LeniencyOption... options)
        throws IOException {
        return getMapper(options).readValue(inputString, JsonNode.class);
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return getMapper().writeValueAsString(object);
            } catch (Exception var2) {
                throw new IllegalArgumentException(var2);
            }
        }
    }

    public static String toPrettyJson(Object object) {
        return toPrettyJson(object, false);
    }

    public static String toPrettyJson(Object object, boolean outputNullMapValues) {
        if (object == null) {
            return null;
        } else {
            try {
                return outputNullMapValues ? JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.writeValueAsString(object) : JSON_PRETTY_MAPPER.writeValueAsString(object);
            } catch (Exception var3) {
                throw new IllegalArgumentException(var3);
            }
        }
    }

    public static String toPrettyJson(String inputJson) {
        return toPrettyJson(inputJson, false);
    }

    public static String toPrettyJson(String inputJson, boolean outputNullMapValues) {
        if (inputJson == null) {
            return null;
        } else {
            try {
                return toPrettyJson(parseJson(inputJson, LeniencyOption.ALLOW_DUPS, LeniencyOption.ALLOW_UNKNOWN), outputNullMapValues);
            } catch (Exception var3) {
                log.debug("Unable to reformat Json, returning original Json", var3);
                return inputJson;
            }
        }
    }

    public enum LeniencyOption {
        ALLOW_DUPS,
        ALLOW_UNKNOWN
    }

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(
            ZonedDateTime.class,
            new JsonDeserializer<>() {
                public ZonedDateTime deserialize(
                    JsonParser json, DeserializationContext context) {
                    String text;
                    try {
                        text = json.getText();
                    } catch (Exception var6) {
                        throw new IllegalArgumentException(var6);
                    }

                    if (text == null) {
                        return null;
                    } else {
                        try {
                            return ZonedDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(text)), ZoneOffset.UTC);
                        } catch (Exception var5) {
                            JsonUtil.log.debug("Unable to deserialize dateTime using ZonedDateTime.ofInstant, will try using ZonedDateTime.parse.", var5);
                            return ZonedDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
                        }
                    }
                }
            });
        module.addDeserializer(
            Instant.class,
            new JsonDeserializer<>() {
                public Instant deserialize(JsonParser json, DeserializationContext context) {
                    String text;
                    try {
                        text = json.getText();
                    } catch (Exception var5) {
                        throw new IllegalArgumentException(var5);
                    }
                    return text == null ? null : Instant.parse(text);
                }
            });
        module.addSerializer(
            Instant.class,
            new JsonSerializer<>() {
                public void serialize(
                    Instant instant,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider) {
                    if (instant != null) {
                        String string = instant.toString();
                        try {
                            jsonGenerator.writeString(string);
                        } catch (Exception var6) {
                            throw new IllegalArgumentException(var6);
                        }
                    }
                }
            });
        module.addSerializer(
            ZonedDateTime.class,
            new JsonSerializer<>() {
                public void serialize(
                    ZonedDateTime src, JsonGenerator arg1, SerializerProvider arg2) {
                    String string = src.withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
                    try {
                        arg1.writeString(string);
                    } catch (Exception var6) {
                        throw new IllegalArgumentException(var6);
                    }
                }
            });
        addMapper(module);
        addMapper(module, LeniencyOption.ALLOW_DUPS);
        addMapper(module, LeniencyOption.ALLOW_UNKNOWN);
        addMapper(module, LeniencyOption.ALLOW_DUPS, LeniencyOption.ALLOW_UNKNOWN);
        JSON_PRETTY_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        JSON_PRETTY_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_PRETTY_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON_PRETTY_MAPPER.findAndRegisterModules();
        JSON_PRETTY_MAPPER.registerModule(module);
        JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS));
        JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.findAndRegisterModules();
        JSON_PRETTY_WRITE_NULL_MAP_VALUES_MAPPER.registerModule(module);
    }
}
