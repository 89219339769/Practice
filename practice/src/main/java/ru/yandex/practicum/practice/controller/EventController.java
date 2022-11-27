package ru.yandex.practicum.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.practice.model.Client;
import ru.yandex.practicum.practice.model.Event;
import ru.yandex.practicum.practice.model.Product;
import ru.yandex.practicum.practice.service.ClientService;
import ru.yandex.practicum.practice.service.FeedService;

import java.util.List;



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {

    private final FeedService feedService;


    @GetMapping()
    public List<Event> getAllEvents() {
        return feedService.getAllEvents();
    }


    @GetMapping("/{id}")
    public List<Event> findClientsById(@PathVariable Long id) {
        return feedService.getByUserId(id);
    }
}