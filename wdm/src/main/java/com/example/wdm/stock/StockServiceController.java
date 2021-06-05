package com.example.wdm.stock;
import io.dapr.actors.client.ActorClient;
import org.springframework.web.bind.annotation.*;
import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StockServiceController {
    StockService stockService = new StockService();

    @GetMapping("/stock/find/{item_id}")
    public String findItem(@PathVariable(name="item_id") String item_id) {
        Map<String, String> res =  stockService.findItem(item_id);
        String json = "{\"stock\":"+res.get("stock")+","+"\"price\":"+res.get("price")+"}";
        return json;
    }

    @PostMapping("/stock/subtract/{item_id}/{number}")
    public String subtractStock(@PathVariable(name="item_id") String item_id, @PathVariable(name="number") Integer number) {
        Map<String, String> res =  stockService.subtractStock(item_id, number);
        String json = "{\"item_id\":"+res.get("item_id")+","+"\"stock\":"+res.get("stock")+"}";
        return json;
    }

    @RequestMapping("/stock/add/{item_id}/{number}")
    public String addStock(@PathVariable(name="item_id") String item_id, @PathVariable(name="number") Integer number) {
        Map<String, String> res =  stockService.addStock(item_id, number);
        String json = "{\"item_id\":"+res.get("item_id")+","+"\"stock\":"+res.get("stock")+"}";
        return json;
    }

    @PostMapping("/stock/item/create/{price}")
    public String createItem(@PathVariable(name="price") Double price) {
        String json =  stockService.createItem(price);
        return json;
    }
}
