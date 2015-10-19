/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: JsonUtils.java, v 0.1 Jun 21, 2015 10:59:27 AM yiliang.gyl Exp $
 */
public class JsonUtils {
    /**  */
    private final static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }

    public static String serialize(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public static Object deserialize(String jsonText, TypeReference type) {
        try {
            return mapper.readValue(jsonText, type);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
