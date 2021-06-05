package com.example.wdm.order;

import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class OrderController {
    OrderService orderService = new OrderService();
    @PostMapping("/orders/create/{user_id}")
    public String create_order(@PathVariable(name = "user_id") String user_id) {
        Map<String,String> result=orderService.createOrderService(user_id);
        String json = "{\"order_id\":" + result.get("order_id") + "}";
        return json;
    }

    @DeleteMapping("/orders/remove/{order_id}")
    public String remove_order(@PathVariable(name = "order_id") String order_id) {
        Map<String,String> mapResult=orderService.removeOrderService(order_id);
        String result = mapResult.get("result");
        return result;
    }

    @RequestMapping("/orders/find/{order_id}")
    public String find_order(@PathVariable(name = "order_id") String order_id) {
        String result = "";
        Map<String,String> mapResult = orderService.findOrderService(order_id);
        if(mapResult.get("status").equals("success")){
            result = "{\"order_id\":" + mapResult.get("order_id") + "," + "\"paid\":" + mapResult.get("paid") + "\"items\":"
                    + mapResult.get("items") + "\"user_id\":" + mapResult.get("user_id") + "\"total_cost\":" + mapResult.get("total_cost") + "}";
        }
        else{
            result = mapResult.get("result");
        }
        return result;
    }

    @PostMapping("/orders/addItem/{order_id}/{item_id}")
    public String addOrders(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        Map<String,String> mapResult = orderService.addOrderService(order_id, item_id);

        String result = mapResult.get("result");
        return result;
    }

    @DeleteMapping("/orders/removeItem/{order_id}/{item_id}")
    public String remove_item(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        Map<String,String> mapResult = orderService.removeOrderService(order_id,item_id);
        String result = mapResult.get("result");
        return result;
    }

    @PostMapping("/orders/checkout/{order_id}")
    public String checkout(@PathVariable(name = "order_id") String order_id) {
        String paymentResult = new String();
        String stockResult = orderService.checkout(order_id).get("result");
        return stockResult;
    }

}


