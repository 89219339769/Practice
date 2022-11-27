package ru.yandex.practicum.practice.exceptions;

public class NameNullException extends RuntimeException {
    public NameNullException(String message) {
        super(message);
    }
}