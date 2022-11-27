package ru.yandex.practicum.practice.service;

import ch.qos.logback.core.net.server.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.practice.model.Event;
import ru.yandex.practicum.practice.model.EventTypes;
import ru.yandex.practicum.practice.model.OperationTypes;
import ru.yandex.practicum.practice.model.SalesMan;
import ru.yandex.practicum.practice.storage.SalesManStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
@Service
public class SalesManService {
SalesManStorage salesManStorage;
    @Autowired
    public SalesManService(SalesManStorage salesManStorage) {
        this.salesManStorage = salesManStorage;
    }

    public SalesMan addSalesMan(SalesMan salesMan) {
       return salesManStorage. addSalesMan( salesMan);
    }



    public SalesMan findSalesManById (Long id) {




        return salesManStorage. findSalesManById(id);
    }


    public List<SalesMan> findAllSalesMans() {
        return salesManStorage.findAllSalesMans();
    }

}


