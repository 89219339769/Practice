package ru.yandex.practicum.practice.exceptions;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class ObjectWrongEnterExeption extends MethodArgumentTypeMismatchException {

    public ObjectWrongEnterExeption(Object value, Class<?> requiredType, String name, MethodParameter param, Throwable cause) {
        super(value, requiredType, name, param, cause);
    }
}
