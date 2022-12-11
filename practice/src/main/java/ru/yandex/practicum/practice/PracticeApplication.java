package ru.yandex.practicum.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.practice.storage.ClientStorage;

@SpringBootApplication
public class PracticeApplication {

	public static void main(String[] args) {

		SpringApplication.run(PracticeApplication.class, args);
		System.out.println("hello");
	}

}
