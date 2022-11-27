package ru.yandex.practicum.practice.vadidate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.practice.exceptions.NameNullException;
import ru.yandex.practicum.practice.model.Client;

@Component
public class NameBig implements Validation {
    @Override
    public void validate(Client client) {
        if (client.getName().length()>100) {
            throw new NameNullException("имя слижком длинное");
        }
    }
}
