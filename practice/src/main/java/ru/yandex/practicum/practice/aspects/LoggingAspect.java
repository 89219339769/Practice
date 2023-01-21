package ru.yandex.practicum.practice.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.practice.model.Client;
import ru.yandex.practicum.practice.model.Event;
import ru.yandex.practicum.practice.model.EventTypes;
import ru.yandex.practicum.practice.model.OperationTypes;
import ru.yandex.practicum.practice.service.FeedService;
import ru.yandex.practicum.practice.storage.ClientStorage;
import ru.yandex.practicum.practice.storage.FeedStorage;

import java.util.List;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    private final ClientStorage clientStorage;
    private final FeedService feedService;
    private final FeedStorage feedStorage;

    public LoggingAspect(FeedService feedService, FeedStorage feedStorage, ClientStorage clientStorage) {
        this.feedService = feedService;
        this.feedStorage = feedStorage;
        this.clientStorage = clientStorage;
    }


    @AfterReturning("execution(public  * ru.yandex.practicum.practice.storage.ClientStorage.addClient" +
            "(ru.yandex.practicum.practice.model.Client)))")

    public void afterAddClient() {
        List<Client> list = clientStorage.findAllClients();
        Client client = list.get(list.size() - 1);
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .clientId(Long.valueOf(client.getId()))
                .eventType(EventTypes.CLIENT)
                .operation(OperationTypes.ADD)
                .eventId(0L)
                .build();
        feedStorage.addEvent(event);

        log.info("был  добавлен клиент " + client);
    }


    //добавить продукт логинг и удаление продукта
//    @AfterReturning("execution(public  * ru.yandex.practicum.practice.storage.ClientStorage.addClient" +
//            "(ru.yandex.practicum.practice.model.Client)))")


    @AfterReturning("execution(public  * ru.yandex.practicum.practice.storage.ClientStorage.findClientById" +
            "(Long))")
    public void afterGetProductById(JoinPoint jp) {
        Long clientId = (Long) jp.getArgs()[0];

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .clientId(clientId)
                .eventType(EventTypes.CLIENT)
                .operation(OperationTypes.GET)
                .eventId(0L)
                .build();
        feedStorage.addEvent(event);

        log.info("проверяем вынос в настройки");

        log.info("был  взят клиент " + clientId);
    }


}














