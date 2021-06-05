package com.example.wdm.order;

import com.example.wdm.payment.CallActor;
import com.example.wdm.payment.PaymentActor;
import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;
import io.dapr.actors.runtime.ActorRuntime;
import io.dapr.v1.DaprProtos.DeleteStateRequest;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@RestController
public class OrderController {
    OrderService orderService = new OrderService();
    @PostMapping("/orders/create/{user_id}")
    public String create_order(@PathVariable(name = "user_id") String user_id) {
        String json = orderService.createOrderService(user_id);
        return json;
    }

    @DeleteMapping("/orders/remove/{order_id}")
    public String remove_order(@PathVariable(name = "order_id") String order_id) {
        String result = orderService.removeOrderService(order_id);
        return result;
    }

    @RequestMapping("/orders/find/{order_id}")
    public String find_order(@PathVariable(name = "order_id") String order_id) {
        String json = orderService.findOrderService(order_id);
        return json;
    }

    @PostMapping("/orders/addItem/{order_id}/{item_id}")
    public String addOrders(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        String result = orderService.addOrderService(order_id, item_id);
        return result;
    }

    @DeleteMapping("/orders/removeItem/{order_id}/{item_id}")
    public String remove_item(@PathVariable(name = "order_id") String order_id, @PathVariable(name = "item_id") String item_id) {
        String result = orderService.removeOrderService(order_id,item_id);
        return result;
    }

    @RequestMapping("/orders/checkout/{order_id}")
    public String index6() {
        return "/orders/checkout/{order_id}";
    }

}


