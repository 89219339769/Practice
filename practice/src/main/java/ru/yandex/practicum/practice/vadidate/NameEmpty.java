package ru.yandex.practicum.practice.vadidate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.practice.exceptions.NameNullException;
import ru.yandex.practicum.practice.model.Client;
@Component
public class NameEmpty implements Validation {
    @Override
    public void validate(Client client) {
        if (client.getName().isBlank()) {
            throw new NameNullException("имя не должно быть пустым");
        }
    }
}