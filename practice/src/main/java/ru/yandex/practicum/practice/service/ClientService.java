package ru.yandex.practicum.practice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.practice.model.*;
import ru.yandex.practicum.practice.storage.ClientStorage;
import ru.yandex.practicum.practice.storage.FeedStorage;
import ru.yandex.practicum.practice.vadidate.Validation;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientService {
    private final ClientStorage clientStorage;
    private final FeedStorage feedStorage;
    private final FeedService feedService;

    List<Validation> validations;

    @Autowired
    public ClientService(ClientStorage clientStorage, FeedStorage feedStorage, FeedService feedService) {
        this.clientStorage = clientStorage;
        this.feedStorage = feedStorage;
        this.feedService = feedService;
    }


    public Client findClientById(Long id) {
        return clientStorage.findClientById(id);
    }

    public List<Client> findAllClients() {
        return clientStorage.findAllClients();
    }

    public Client addClient(Client client) {

        validations.
                forEach(validator -> validator.validate(client));

        return clientStorage.addClient(client);

    }


    public List<Product> findProductsbyId(Long id) {
        return clientStorage.findProductsbyId(id);
    }

    public List<Client> findBestClients(Integer count) {
        List<Client> best = findAllClients().stream()
                .sorted((c1, c2) ->
                        (sumOfClientPurchases(c2) - sumOfClientPurchases(c1)))
                .limit(count)
                .collect(Collectors.toList());
        return best;
    }

    public int sumOfClientPurchases(Client client) {
        int price = 0;
        List<Product> list = client.getProducts();
        for (int i = 0; i < list.size(); i++) {
            price = price + list.get(i).getPrice();
        }
        return price;
    }


    public ClientRating findRatingById(Long id) {
        return clientStorage.findRatingById(id);
    }

    public List<Product> getCommonProducts(Long id, Long otherId) {
        List<Product> first = clientStorage.findProductsbyId(id);
        List<Product> second = clientStorage.findProductsbyId(otherId);


        return first.stream()
                .filter(second::contains)
                .collect(Collectors.toList());
    }


    public boolean addGoodReviews(Integer clientId, Integer salesManId) {
        return clientStorage.addGoodReviews(clientId, salesManId);
    }


    public List<Client> findClientByProduct(Integer id) {


        return clientStorage.findClientByProduct(id);
    }

    public List<Client> findClientByName(String name) {

        return clientStorage.findClientByName(name);

    }


    public List<Product> getRcomendations(Integer id) {
        List<Product> products = clientStorage.getRcomendations(id);
        return products;

    }


    public boolean addLikeProduct(Integer ClientId, Integer Product_id) {
        return clientStorage.addLikeProduct(ClientId, Product_id);


    }


    public void removeRewiew(Integer clientId, Integer salesManId) {
        log.info("удален отзыв клиент" + clientId);
        clientStorage.removeRewiew(clientId, salesManId);
    }


    public void removeLike(Integer ClientId, Integer ProductId) {
        clientStorage.removeLike(ClientId, ProductId);
    }
}