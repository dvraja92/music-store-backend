package com.decipherzone.dropwizard.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;

import java.io.InputStream;

@Singleton
public final class JsonSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonSerializer(){}


    public static String toJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        }
        catch (JsonProcessingException e) {
            return null;
        }
    }


    public static <T> T fromBytes(byte[] bytes, Class<? extends T> clazz) {
        try {
            return mapper.readValue(bytes,clazz);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<? extends T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(InputStream stream, Class<? extends T> clazz) {
        try {
            return mapper.readValue(stream,clazz);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static <T> T convert(Object object, Class<? extends T> clazz) {
        try {
            return mapper.convertValue(object, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}

