package com.sp.asyncj.test.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

public class TestUtils {
    public static Object readField(Object obj, String field) throws IllegalAccessException {
        final String[] parts = field.split("\\.");
        Object target = obj;
        for (String part : parts) {
            target = FieldUtils.readField(target, part, true);
        }
        return target;
    }
}
