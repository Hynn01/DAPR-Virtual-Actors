package com.example.wdm.payment;

import io.dapr.actors.client.ActorClient;

import net.minidev.json.JSONObject;
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

    @PostMapping(value="/payment/pay/{user_id}/{order_id}/{amount}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String postPayment(@PathVariable(name="user_id") String user_id, @PathVariable(name="amount") Double amount) {
        Map<String,String> res=PaymentService.postPayment(user_id,amount);
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        result.put("credit", res.get("credit"));
        return result.toJSONString();
//        String json =  "{\"user_id\":"+result.get("user_id")+","+"\"credit\":"+result.get("credit")+"}";
//        return json;
    }

    @PostMapping(value="/payment/cancel/{user_id}/{order_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String cancelPayment(@PathVariable(name="user_id") String user_id, @PathVariable(name="order_id") String order_id) {

        String res=PaymentService.cancelPayment(user_id,order_id);
        JSONObject result = new JSONObject();
        result.put("status", res);
        return result.toJSONString();
//        return result;
    }

    @GetMapping(value = "/payment/status/{order_id}",produces ="application/json;charset=UTF-8")
    @ResponseBody
    public String getPaymentStatus(@PathVariable(name="order_id") String order_id) {

        String res=PaymentService.getPaymentStatus(order_id);
        JSONObject result = new JSONObject();
        result.put("status", res);
        return result.toJSONString();
    }

    @PostMapping(value="/payment/add_funds/{user_id}/{amount}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String addFunds(@PathVariable(name="user_id") String user_id, @PathVariable(name="amount") Double amount) {
        System.out.println("amount: "+amount);
        Map<String,String> res=PaymentService.addFunds(user_id,amount);
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        result.put("credit", res.get("credit"));
        return result.toJSONString();
//        String json =  "{\"user_id\":"+result.get("user_id")+","+"\"credit\":"+result.get("credit")+"}";
//        return json;
    }
    @PostMapping(value="/payment/create_user",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String createUser() {
        Map<String,String> res=PaymentService.createUser();
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        return result.toJSONString();
//        String json =  "{\"user_id\":"+result.get("user_id")+"}";
//        return json;
    }

    @GetMapping(value="/payment/find_user/{user_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String findUser(@PathVariable(name="user_id") String user_id) {
        Map<String,String> res=PaymentService.findUser(user_id);
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        result.put("credit", res.get("credit"));
        return result.toJSONString();
//        String json =  "{\"user_id\":"+result.get("user_id")+","+"\"credit\":"+result.get("credit")+"}";
//        return json;
    }
}
