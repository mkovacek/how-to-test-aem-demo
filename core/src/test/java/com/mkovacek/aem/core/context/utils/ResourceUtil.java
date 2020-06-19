package com.mkovacek.aem.core.context.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class ResourceUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getExpectedResult(final Class<?> clazz, final String resourcePath) throws IOException {
        final InputStream inputStream = clazz.getResourceAsStream(resourcePath);
        final JsonNode jsonNode = objectMapper.readValue(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonNode.class);
        inputStream.close();
        return jsonNode.toString();
    }

    private ResourceUtil() {
    }

}