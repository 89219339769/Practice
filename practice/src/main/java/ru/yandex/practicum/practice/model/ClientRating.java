package ru.yandex.practicum.practice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRating {
    private Long id;
    private Integer rate;
    private String name;
}