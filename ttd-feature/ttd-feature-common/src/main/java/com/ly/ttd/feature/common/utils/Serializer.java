package com.ly.ttd.feature.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ly.ttd.utils.impl.JacksonSerializer;

/**
 *
 */

public interface Serializer {

    String serializeAsString(Object obj);

    byte[] serializeAsBytes(Object obj);

    <T> T deserialize(Class<T> clazz, String serializeString);

    <T> T deserialize(Class<T> clazz, byte[] serializeBytes);

    <T> T deserialize(TypeReference<T> typeReference, String serializeString);

    JacksonSerializer INSTANCE = new JacksonSerializer();

    JacksonSerializer PRETTY_INSTANCE = new JacksonSerializer(true);

    JacksonSerializer INCLUDE_NULL_INSTANCE = new JacksonSerializer(false, true);

    JacksonSerializer PRETTY_INCLUDE_NULL_INSTANCE = new JacksonSerializer(true, true);
}
