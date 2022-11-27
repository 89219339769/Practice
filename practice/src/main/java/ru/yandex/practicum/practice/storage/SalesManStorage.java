package ru.yandex.practicum.practice.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.practice.model.Client;
import ru.yandex.practicum.practice.model.Product;
import ru.yandex.practicum.practice.model.SalesMan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class SalesManStorage {

    private final JdbcTemplate jdbcTemplate;

    public SalesMan addSalesMan(SalesMan salesMan) {
        String sqlQuery = "insert into sales_mans( Name, last_name) values ( ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, salesMan.getName());
            ps.setString(2, salesMan.getLastName());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();


        return findSalesManById(id);
    }



    public SalesMan findSalesManById (Long id) {
        String sqlQuery = "select * from sales_mans where id = ?";
        SalesMan salesMan;
        //  client {
        salesMan = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToSalesMan, id);
        //  } catch (EmptyResultDataAccessException e) {
        //      throw new ObjectNotFoundException("Пользователь с id " + id + " не найден.");
        //    }

        return salesMan;
    }


    private SalesMan mapRowToSalesMan(ResultSet resultSet, int rowNum) throws SQLException {
        return SalesMan.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .lastName(resultSet.getString("last_name"))
                .build();
    }




    public List<SalesMan> findAllSalesMans() {
        String sqlQuery = "select * from sales_mans";
        return jdbcTemplate.query(sqlQuery, this:: mapRowToSalesMan);
    }





}
