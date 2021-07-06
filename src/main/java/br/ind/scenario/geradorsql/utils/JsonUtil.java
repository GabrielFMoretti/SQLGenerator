package br.ind.scenario.geradorsql.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonUtil {

    public static <T> Optional<T> fromJson(String json, Class<T> objectClass) {
        try {
            return Optional.of(new ObjectMapper().readValue(json, objectClass));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> toJson(Object object) {
        try {
            return Optional.of(new ObjectMapper().writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
