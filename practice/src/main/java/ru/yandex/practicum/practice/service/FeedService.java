package ru.yandex.practicum.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.practice.model.Event;
import ru.yandex.practicum.practice.storage.FeedStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;

    public List<Event> getByUserId(Long userId) {
        return feedStorage.getByClientId(userId);
    }



    public List<Event> getAllEvents() {
  return feedStorage.getAllEvents();
    }


}
