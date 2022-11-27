package ru.yandex.practicum.practice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@Builder
 public class Event {
    private Long timestamp;
    private Long clientId;
    private EventTypes eventType;
    private OperationTypes operation;
    private Long eventId;
    private Integer  entityId;
}