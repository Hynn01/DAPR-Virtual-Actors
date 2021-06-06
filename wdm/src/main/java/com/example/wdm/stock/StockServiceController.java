package com.example.wdm.stock;
import io.dapr.actors.client.ActorClient;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping(value="/stock/find/{item_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String findItem(@PathVariable(name="item_id") String item_id) {
        Map<String, String> res =  stockService.findItem(item_id);
        //System.out.println(res.get("stock"));
        JSONObject result = new JSONObject();
        result.put("stock", res.get("stock"));
        result.put("price", res.get("price"));
        return result.toJSONString();
//        String json = "{\"stock\":"+res.get("stock")+","+"\"price\":"+res.get("price")+"}";
//        return json;
    }

    @PostMapping(value="/stock/subtract/{item_id}/{number}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String subtractStock(@PathVariable(name="item_id") String item_id, @PathVariable(name="number") Integer number) {
        Map<String, String> res =  stockService.subtractStock(item_id, number);
        //String json = "{\"item_id\":"+res.get("item_id")+","+"\"stock\":"+res.get("stock")+"}";
        JSONObject result = new JSONObject();
        result.put("item_id", res.get("item_id"));
        result.put("stock", res.get("stock"));
        return result.toJSONString();
//        String json = "{\"item_id\":"+res.get("item_id")+","+"\"stock\":"+res.get("stock")+"}";
//        return json;
    }

    @RequestMapping(value="/stock/add/{item_id}/{number}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String addStock(@PathVariable(name="item_id") String item_id, @PathVariable(name="number") Integer number) {
        Map<String, String> res =  stockService.addStock(item_id, number);
        JSONObject result = new JSONObject();
        result.put("item_id", res.get("item_id"));
        result.put("stock", res.get("stock"));
        return result.toJSONString();
//        String json = "{\"item_id\":"+res.get("item_id")+","+"\"stock\":"+res.get("stock")+"}";
//        return json;
    }

    @PostMapping(value={"/stock/item/create/{price}"},produces="application/json;charset=UTF-8")
    @ResponseBody
    public String createItem(@PathVariable(name="price") Double price) {

        Map<String,String> res =  stockService.createItem(price);
        JSONObject result = new JSONObject();
//        result.put("msg", "ok");
//        result.put("method", "@ResponseBody");
        result.put("item_id", res.get("item_id"));

        return result.toJSONString();
    }
}
