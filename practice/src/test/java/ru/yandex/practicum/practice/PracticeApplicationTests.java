package ru.yandex.practicum.practice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.practice.model.Client;
import ru.yandex.practicum.practice.model.ClientRating;
import ru.yandex.practicum.practice.storage.ClientStorage;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PracticeApplicationTests {
    private final JdbcTemplate jdbcTemplate;
    private final ClientStorage clientStorage;


    private Client getClient() {
        return Client.builder()
                .login("vasia")
                .name("testLogin")
                .email("testName@m.ru")
                .products((new ArrayList<>()))
                .clientRating(ClientRating.builder().id(1L).rate(2).name("loh").build()).build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM Clients");
        jdbcTemplate.update("DELETE FROM client_product");
        jdbcTemplate.update("DELETE FROM products");
        jdbcTemplate.update("DELETE FROM client_rating");

        jdbcTemplate.update("ALTER TABLE Clients ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE Products ALTER COLUMN ID RESTART WITH 1");

    }




    @Test
    void testAddClient() {
       Client client = getClient();
        Client userSaved = clientStorage.addClient(client);
        client.setId(1L);
        assertEquals(client, userSaved);
    }



}


