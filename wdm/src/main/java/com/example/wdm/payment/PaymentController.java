package com.example.wdm.payment;

import io.dapr.actors.client.ActorClient;
import org.springframework.web.bind.annotation.*;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class PaymentController {

//    private static final int NUM_ACTORS = 3;

    @PostMapping("/payment/pay/{user_id}/{order_id}/{amount}")
    public String postPayment(@PathVariable(name="user_id") String user_id, @PathVariable(name="amount") Double amount) {
        Map<String,String> result=PaymentService.postPayment(user_id,amount);
        String json =  "{\"user_id\":"+result.get("user_id")+","+"\"credit\":"+result.get("credit")+"}";
        return json;
    }

    @PostMapping("/payment/cancel/{user_id}/{order_id}")
    public String cancelPayment(@PathVariable(name="user_id") String user_id, @PathVariable(name="order_id") String order_id) {

        PaymentService.cancelPayment(user_id,order_id);
        return "'ok': (true/false)";
    }

    @GetMapping("/payment/status/{order_id}")
    public String getPaymentStatus(@PathVariable(name="order_id") String order_id) {

        PaymentService.getPaymentStatus(order_id);
        return "'paid': (true/false)";
    }

    @PostMapping("/payment/add_funds/{user_id}/{amount}")
    public String addFunds(@PathVariable(name="user_id") String user_id, @PathVariable(name="amount") Double amount) {
        System.out.println("amount: "+amount);
        Map<String,String> result=PaymentService.addFunds(user_id,amount);
        String json =  "{\"user_id\":"+result.get("user_id")+","+"\"credit\":"+result.get("credit")+"}";
        return json;
    }
    @PostMapping("/payment/create_user")
    public String createUser() {
        Map<String,String> result=PaymentService.createUser();
        String json =  "{\"user_id\":"+result.get("user_id")+"}";
        return json;
    }

    @GetMapping("/payment/find_user/{user_id}")
    public String findUser(@PathVariable(name="user_id") String user_id) {
        Map<String,String> result=PaymentService.findUser(user_id);
        String json =  "{\"user_id\":"+result.get("user_id")+","+"\"credit\":"+result.get("credit")+"}";
        return json;
    }
}
