package ru.yandex.practicum.practice.model;

import lombok.*;


import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Client {
    private Long id;
    private String login;
    private String name;

    private String email;
    @NotNull
    private List<Product> products;
    private ClientRating clientRating;
    private List<Long> goodReviews;
}

