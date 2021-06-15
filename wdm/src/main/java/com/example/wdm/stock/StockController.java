package com.example.wdm.stock;

import com.example.wdm.payment.PaymentService;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class StockController {
    @GetMapping(value="/stock/find/{item_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String findItem(@PathVariable(name="item_id") String item_id) {
        Map<String,String> res= StockService.findItem(item_id);
        JSONObject result = new JSONObject();
        result.put("item_id", res.get("item_id"));
        result.put("stock", res.get("stock"));
        result.put("price", res.get("price"));
        return result.toJSONString();
    }
    @PostMapping(value="/stock/subtract/{item_id}/{stock}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String subtractItem(@PathVariable(name="item_id") String item_id, @PathVariable(name="stock") Integer stock) {
        Map<String,String> res = StockService.subtractItem(item_id, stock);
        JSONObject result = new JSONObject();
        result.put("item_id", res.get("item_id"));
        result.put("stock", res.get("stock"));
        return result.toJSONString();
//        return result;
    }

    @PostMapping(value = "/stock/add/{item_id}/{stock}",produces ="application/json;charset=UTF-8")
    @ResponseBody
    public String addItem(@PathVariable(name="item_id") String item_id, @PathVariable(name="stock") Integer stock) {
        Map<String,String> res = StockService.addItem(item_id, stock);
        JSONObject result = new JSONObject();
        result.put("item_id", res.get("item_id"));
        result.put("stock", res.get("stock"));
        return result.toJSONString();
    }


    @PostMapping(value = "/stock/item/create/{price}",produces ="application/json;charset=UTF-8")
    @ResponseBody
    public String createItem(@PathVariable(name="price") Double price) {
        Map<String,String> res = StockService.createItem(price);
        JSONObject result = new JSONObject();
        result.put("item_id", res.get("item_id"));
        return result.toJSONString();
    }
}
