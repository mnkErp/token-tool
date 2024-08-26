package org.mnk.tokentool.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class LocalObjectMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T convertObject(String jsonString, Class<T> objectClass) {

        try {
            JsonNode jsonObj = mapper.readTree(jsonString);
            return mapper.convertValue(jsonObj, objectClass);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    @SneakyThrows
    public static <T> String convertToString(T obj) {

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
}
