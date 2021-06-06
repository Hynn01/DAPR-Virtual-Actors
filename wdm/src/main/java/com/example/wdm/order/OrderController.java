package com.example.wdm.order;

import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class OrderController {
    OrderService orderService = new OrderService();
    @PostMapping("/orders/create/{user_id}")
    public String create_order(@PathVariable(name = "user_id") String user_id) {
        Map<String,String> res=orderService.createOrderService(user_id);
        JSONObject result = new JSONObject();
        result.put("order_id", res.get("order_id"));
        return result.toJSONString();
//        String json = "{\"order_id\":" + result.get("order_id") + "}";
//        return json;
    }

    @DeleteMapping("/orders/remove/{order_id}")
    public String remove_order(@PathVariable(name = "order_id") String order_id) {
        Map<String,String> res=orderService.removeOrderService(order_id);
        JSONObject result = new JSONObject();
        result.put("result", res.get("result"));
        return result.toJSONString();
//        String result = mapResult.get("result");
//        return result;
    }

    @RequestMapping("/orders/find/{order_id}")
    public String find_order(@PathVariable(name = "order_id") String order_id) {

        JSONObject result = new JSONObject();


//        String result = "";
        Map<String,String> mapResult = orderService.findOrderService(order_id);
        if(mapResult.get("status").equals("success")){
            result.put("order_id", mapResult.get("order_id"));
            result.put("paid", mapResult.get("paid"));
            result.put("items", mapResult.get("items"));
            result.put("user_id", mapResult.get("user_id"));
            result.put("total_cost", mapResult.get("total_cost"));
//            result = "{\"order_id\":" + mapResult.get("order_id") + "," + "\"paid\":" + mapResult.get("paid") + "\"items\":"
//                    + mapResult.get("items") + "\"user_id\":" + mapResult.get("user_id") + "\"total_cost\":" + mapResult.get("total_cost") + "}";
        }
        else{
            result.put("result", mapResult.get("result"));
//            result = mapResult.get("result");
        }
        return result.toJSONString();
    }

    @PostMapping("/orders/addItem/{order_id}/{item_id}")
    public String addOrders(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        Map<String,String> res = orderService.addOrderService(order_id, item_id);
        JSONObject result = new JSONObject();
        result.put("result", res.get("result"));
        return result.toJSONString();

//        String result = mapResult.get("result");
//        return result;
    }

    @DeleteMapping("/orders/removeItem/{order_id}/{item_id}")
    public String remove_item(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        Map<String,String> mapResult = orderService.removeOrderService(order_id,item_id);
        JSONObject res = new JSONObject();
        JSONObject result = new JSONObject();
        result.put("result", res.get("result"));
        return result.toJSONString();
//        String result = mapResult.get("result");
//        return result;
    }

    @PostMapping("/orders/checkout/{order_id}")
    public String checkout(@PathVariable(name = "order_id") String order_id) {
        String paymentResult = new String();
        String stockResult = orderService.checkout(order_id).get("result");
        JSONObject result = new JSONObject();
        result.put("result", stockResult);
        return result.toJSONString();
//        return stockResult;
    }

}


