package ru.yandex.practicum.practice.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Product {
    int id;
    String name;
    int price;

}
