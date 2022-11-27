package ru.yandex.practicum.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.practice.model.Client;
import ru.yandex.practicum.practice.model.SalesMan;
import ru.yandex.practicum.practice.service.SalesManService;
import ru.yandex.practicum.practice.storage.ClientStorage;
import ru.yandex.practicum.practice.storage.SalesManStorage;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/salesmans")
public class SalesManController {

    private final SalesManService  salesManService;


    @PostMapping()
    public SalesMan addSilesman(@RequestBody SalesMan salesMan) {
        return  salesManService.addSalesMan(salesMan);
    }



    @GetMapping()
    public List<SalesMan> findAllSalesMans() {
        return  salesManService.findAllSalesMans();
    }


    @GetMapping("/{id}")
    public SalesMan findSalesManById(@PathVariable Long id) {
        return  salesManService.findSalesManById(id);
    }
}
