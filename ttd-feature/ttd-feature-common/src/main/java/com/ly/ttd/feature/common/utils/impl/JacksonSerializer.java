package com.ly.ttd.feature.common.utils.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaVersion;
import com.ly.ttd.utils.Serializer;

import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class JacksonSerializer implements Serializer {

    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
        this(false);
    }

    public JacksonSerializer(boolean pretty) {
        this(pretty, false);
    }

    public JacksonSerializer(boolean pretty, boolean includeNull) {
        this(false, pretty, includeNull, "GMT+8");
    }

    public JacksonSerializer(boolean jacksonSmile, boolean pretty, boolean includeNull, String timeZone) {
        this("yyyyMMddHHmmss", jacksonSmile, pretty, includeNull, timeZone);
    }

    public JacksonSerializer(String datePattern, boolean jacksonSmile, boolean pretty, boolean includeNull, String timeZone) {
        super();
        if (jacksonSmile) {
            objectMapper = new ObjectMapper(new SmileFactory());
        } else {
            objectMapper = new ObjectMapper();
        }
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        if (!includeNull) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        objectMapper.setDateFormat(new SimpleDateFormat(datePattern));
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, pretty);


        boolean registerHibernate5Module = false;
        try {
            Class.forName("com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule");
            Hibernate5JakartaVersion.getHibernateVersion();
            registerHibernate5Module = true;
        } catch (Exception ignore) {
            // ignore
        }
        if (registerHibernate5Module) {
            Hibernate5JakartaModule module = new Hibernate5JakartaModule();
            module.enable(Hibernate5JakartaModule.Feature.USE_TRANSIENT_ANNOTATION);
            objectMapper.registerModule(module);
        }
    }

    public String serializeAsString(Object bean) {
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] serializeAsBytes(Object bean) {
        try {
            return objectMapper.writeValueAsBytes(bean);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(Class<T> clazz, String serializeString) {
        try {
            return objectMapper.readValue(serializeString, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(Class<T> clazz, byte[] serializeBytes) {
        try {
            return objectMapper.readValue(serializeBytes, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public <T> T deserialize(TypeReference<T> typeReference, String serializeString) {
        try {
            return objectMapper.readValue(serializeString, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public JsonNode deserialize(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
