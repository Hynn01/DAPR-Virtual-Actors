package com.example.wdm.order;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.wdm.Exception.OrderException;

import java.util.Map;


@RestController
public class OrderController {
    @PostMapping(value="/orders/create/{user_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String create_order(@PathVariable(name = "user_id") String user_id) {
        Map<String,String> res=OrderService.createOrderService(user_id);
        JSONObject result = new JSONObject();
        result.put("order_id", res.get("order_id"));
        return result.toJSONString();
    }

    @DeleteMapping(value="/orders/remove/{order_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String remove_order(@PathVariable(name = "order_id") String order_id) {
        Map<String,String> res=OrderService.removeOrderService(order_id);
        JSONObject result = new JSONObject();
        result.put("result", res.get("result"));
        return result.toJSONString();
    }

    @RequestMapping(value="/orders/find/{order_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String find_order(@PathVariable(name = "order_id") String order_id) {

        JSONObject result = new JSONObject();
        Map<String,String> mapResult = OrderService.findOrderService(order_id);
        if(mapResult.get("status").equals("success")){
            result.put("order_id", mapResult.get("order_id"));
            result.put("paid", mapResult.get("paid"));
            result.put("items", mapResult.get("items"));
            result.put("user_id", mapResult.get("user_id"));
            result.put("total_cost", mapResult.get("total_cost"));
        }
        else{
            result.put("result", mapResult.get("result"));
        }
        return result.toJSONString();
    }

    @PostMapping(value="/orders/addItem/{order_id}/{item_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String addOrders(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        Map<String,String> res = OrderService.addOrderService(order_id, item_id);
        JSONObject result = new JSONObject();
        result.put("result", res.get("result"));
        return result.toJSONString();
    }

    @DeleteMapping(value="/orders/removeItem/{order_id}/{item_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String remove_item(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        Map<String,String> mapResult = OrderService.removeOrderService(order_id,item_id);
        JSONObject res = new JSONObject();
        JSONObject result = new JSONObject();
        result.put("result", res.get("result"));
        return result.toJSONString();
    }

    @PostMapping(value="/orders/checkout/{order_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String checkout(@PathVariable(name = "order_id") String order_id) throws OrderException{
        String paymentResult = new String();
        String stockResult = OrderService.checkout(order_id).get("result");
        System.out.println("1"+stockResult);
        if(!stockResult.equals("Sufficient stockEnough credit")){
            throw new OrderException();
        }
        JSONObject result = new JSONObject();
        result.put("result", stockResult);
        System.out.println("3"+stockResult);
        return result.toJSONString();
    }
}


