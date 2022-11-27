package ru.yandex.practicum.practice.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.practice.exceptions.ObjectNotFoundException;

import ru.yandex.practicum.practice.exceptions.ObjectWrongEnterExeption;
import ru.yandex.practicum.practice.model.*;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Component
public class ClientStorage {


    FeedStorage feedStorage;
    private final JdbcTemplate jdbcTemplate;

    public ClientStorage(JdbcTemplate jdbcTemplate, FeedStorage feedStorage) {

        this.jdbcTemplate = jdbcTemplate;
        this.feedStorage=feedStorage;
    }

    public Client findClientById(Long id) {
        String sqlQuery = "select * from clients where id = ?";
        Client client;
        try {
            client = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToClient, id);
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден.");
        }

        return client;
    }

    public List<Client> findAllClients() {
        String sqlQuery = "select * from clients";
        return jdbcTemplate.query(sqlQuery, this::mapRowToClient);
    }

    public Client addClient(Client client) {
        String sqlQuery = "insert into CLIENTS( LOGIN, NAME, email, rating_id) values ( ?, ?, ?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, client.getLogin());
            ps.setString(2, client.getName());
            ps.setString(3, client.getEmail());
            ps.setInt(4, Math.toIntExact(client.getClientRating().getId()));
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (client.getProducts() != null) {
            for (Product product : client.getProducts()) {
                String sql = "insert into client_product values (?, ?)";
                jdbcTemplate.update(sql,
                        id,
                        product.getId());
            }
        }
        return findClientById(id);
    }

    private Client mapRowToClient(ResultSet resultSet, int rowNum) throws SQLException {
        return Client.builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .products(findProductsbyId(resultSet.getLong("id")))
                .clientRating(findRatingById((long) resultSet.getInt("rating_id")))
                .goodReviews(findSalesManIdWhoLikedClient((long) resultSet.getInt("id")))
                .build();
    }


    public List<Product> findProductsbyId(long id) {
        String sqlQuery = "select p.product_id, p.name, p.price from products as p\n" +
                "inner join client_product as cp on p.product_id = cp.product_id\n" +
                "inner join clients as cl on cl.id = cp.client_id\n" +
                "where cp.client_id =?;";


        return jdbcTemplate.query(sqlQuery, this::mapRowToProduct, id);
    }


    private Product mapRowToProduct(ResultSet resultSet, int rowNum) throws SQLException {
       Product product= Product.builder()
                .id(resultSet.getInt("product_id"))
                .name(resultSet.getString("name"))
                .price(resultSet.getInt("price"))
                .build();
       return product;
    }

    public ClientRating findRatingById(Long id) {
        String sqlQuery = "select * from client_rating where id = ?";
        ClientRating clientRating;
        //  try {
        clientRating = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToClientRating, id);
        //    } catch (EmptyResultDataAccessException e) {
        //
        //        throw new ObjectNotFoundException(String.format("Жанр с id=%d не найден.", id));
        //     }
        return clientRating;
    }


    private ClientRating mapRowToClientRating(ResultSet resultSet, int rowNum) throws SQLException {
        return ClientRating.builder()
                .id(resultSet.getLong("id"))
                .rate(resultSet.getInt("rate"))
                .name(resultSet.getString("name"))
                .build();
    }

    public boolean addGoodReviews(Integer clientId, Integer salesManId) {
        String sqlQuery = "insert into goodreviews(client_Id, salesman_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, clientId, salesManId);


        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .clientId(Long.valueOf(clientId))
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.ADD)
                .entityId(salesManId)
                .eventId(0L)
                .build();

        feedStorage.addEvent(event);

        return true;
    }

    public boolean addLikeProduct(Integer clientId, Integer product_id) {
        String sqlQuery = "insert into likes(client_Id, product_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,clientId,  product_id);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .clientId(Long.valueOf(clientId))
                .eventType(EventTypes.LIKE)
                .operation(OperationTypes.ADD)
                .entityId(product_id)
                .eventId(0L)
                .build();

        feedStorage.addEvent(event);


        return true;
    }


    private List<Long> findSalesManIdWhoLikedClient(Long ClientId) {
        String sqlQuery = "select salesman_ID from goodreviews where client_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, ClientId);
    }

    //  public List<Client> findBestClients(Integer count) {
    //     String sqlQuery = "select id, login, name, email, rating_id\n" +
    //            "from Clients cl \n" +
    //            "left join client_product cp on cl.ID = cp.client_ID \n" +
    //            "group by cl.id\n" +
    //             "order by sum(cp.product_ID) desc limit ?";
    //     return jdbcTemplate.query(sqlQuery, this::mapRowToClient, count);
    //  }

    public List<Client> findClientByProduct(Integer id) {
        String sqlQuery = "select cl.id, cl.login, cl.name, cl.email, cl.rating_id from clients cl \n" +
                "left join client_product cp on cl.ID = cp.client_ID\n" +
                "left join products pr on pr.product_id = cp.product_id\n" +
                "where pr.product_id = ? " +
                "order by cl.rating_id desc";
        if (jdbcTemplate.query(sqlQuery, this::mapRowToClient, id) == null) {
            throw new ObjectNotFoundException(String.format("Фильм с id=%d не найден."));
        }
        return jdbcTemplate.query(sqlQuery, this::mapRowToClient, id);

    }
//
    public List<Client> findClientByName(String name) {
        String sqlQuery = "select cl.id, cl.login, cl.name, cl.email, cl.rating_id\n" +
                "from clients cl\n" +
                "WHERE UPPER(cl.name) ILIKE '%'||?||'%'";

        return jdbcTemplate.query(sqlQuery, this::mapRowToClient, name);

    }



    public List<Product> getRcomendations(long id) {

        final String sql = "select  p2.product_id, p2.name, p2.price\n" +
                "from products p\n" +
                "JOIN LIKES L ON p.product_id = L.product_id AND L.client_id = ?\n" +
                "JOIN LIKES L2 ON L.product_id = L2.product_id AND L2.client_id <> L.client_id\n" +
                "JOIN LIKES L3 ON L2.client_id = L3.client_id AND L3.client_id <> L.client_id\n" +
                "LEFT JOIN LIKES L4 ON L3.product_id = L4.product_id AND L4.client_id = L.client_id\n" +
                "JOIN products p2 ON L3.product_id = p2.product_id\n" +
                "WHERE L4.client_id IS NULL\n" +
                "GROUP BY p2.product_id";
        return jdbcTemplate.query(sql, this::mapRowToProduct, id);
    }

    public void removeRewiew(Integer clientId, Integer salesManId){
        String sqlQuery = "DELETE FROM goodreviews WHERE client_id = ? AND salesman_id = ?;";
        jdbcTemplate.update(sqlQuery, clientId, salesManId);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .clientId(Long.valueOf(clientId))
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.REMOVE)
                .entityId(salesManId)
                .eventId(0L)
                .build();

        feedStorage.addEvent(event);




    }


    public void removeLike(Integer clientId, Integer productId){
        String sqlQuery = "DELETE FROM likes WHERE client_id = ? AND product_id = ?;";
        jdbcTemplate.update(sqlQuery, clientId, productId);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .clientId(Long.valueOf(clientId))
                .eventType(EventTypes.LIKE)
                .operation(OperationTypes.REMOVE)
                .entityId(productId)
                .eventId(0L)
                .build();

        feedStorage.addEvent(event);





    }

}