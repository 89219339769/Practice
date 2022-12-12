package ru.yandex.practicum.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.practice.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.practice.model.Client;
import ru.yandex.practicum.practice.model.Product;
import ru.yandex.practicum.practice.service.ClientService;
import ru.yandex.practicum.practice.storage.ClientStorage;
import ru.yandex.practicum.practice.storage.SalesManStorage;

import java.util.List;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;


    @GetMapping("/best/{count}")
    public List<Client> findBestClient(@PathVariable Integer count) {
        log.info("Получены лучшие клиенты");
        return clientService.findBestClients(count);

    }

    @PostMapping()
    public Client addClient(@RequestBody Client client) {
        return clientService.addClient(client);
    }

    @GetMapping("/{id}/common/{otherId}")
    public List<Product> findCommonProduct(@PathVariable Long id, @PathVariable Long otherId) {
        return clientService.getCommonProducts(id, otherId);
    }

    @GetMapping()
    public List<Client> findAllClients() {

        log.info("Получены все клиенты");

        return clientService.findAllClients();
    }


    @GetMapping("/{id}")
    public Client findUserById(@PathVariable Long id) {
        return clientService.findClientById(id);
    }


    @PutMapping("/{id}/godReviews/{userId}")
    public boolean saveReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return clientService.addGoodReviews(id, userId);
    }

    @GetMapping("/product/{id}")
    public List<Client> findClientByProduct(@PathVariable Integer id) {
        return clientService.findClientByProduct(id);
    }

    // @ExceptionHandler
// отлавливаем исключение IllegalArgumentException
    //  public Map<String, String> handleNegativeCount(final IllegalArgumentException e) {
    // возвращаем сообщение об ошибке
    //      return Map.of("error", "Передан отрицательный параметр count.");
    //   }


//    @GetMapping("/search/{name}")
//    public List<Client> findClientByProduct(@PathVariable String name) {
//
//        return clientService.findClientByName(name);
//    }

    @GetMapping("search")
    public List<Client> findFilmsBySearch(@RequestParam(name = "query") String query) {

        return clientService.findClientByName(query);
    }

    @GetMapping("/{id}/recommendations")
    public List<Product> findRecommendedProduct(@PathVariable(value = "id") Integer id) {
        log.info("Получен запрос к эндпоинту: /users/{id}/recommendations, метод GET");

        return clientService.getRcomendations(id);
    }

    @PutMapping("/{id}/like/{productId}")
    public boolean saveLikeProduct(@PathVariable Integer id, @PathVariable Integer productId) {
        return clientService.addLikeProduct(id, productId);
    }

    @DeleteMapping(value = "/{id}/review/{salesManId}")
    public void removeRevew(@PathVariable Integer id, @PathVariable Integer salesManId) {
        clientService.removeRewiew(id,salesManId);
    }



    @DeleteMapping(value = "/{id}/like/{productId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer productId) {
        clientService.removeLike(id,productId);
    }


}




