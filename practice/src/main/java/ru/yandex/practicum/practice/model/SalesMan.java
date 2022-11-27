package ru.yandex.practicum.practice.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesMan {
    private long id;
    private String name;
    private String lastName;
}
