package com.rafaelhosaka.shareme.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rafaelhosaka.shareme.post.Post;

public class JsonConverter {
    public static Object convertJsonToObject(String json, Class type) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, type);
    }
}
