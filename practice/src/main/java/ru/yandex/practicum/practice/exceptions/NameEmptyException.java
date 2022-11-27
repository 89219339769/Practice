package ru.yandex.practicum.practice.exceptions;

public class NameEmptyException extends RuntimeException {
    public NameEmptyException(String message) {
        super(message);
    }
}