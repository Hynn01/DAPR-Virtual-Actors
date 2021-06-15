package com.example.wdm.payment;

import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class PaymentController {

    @PostMapping(value="/payment/pay/{user_id}/{order_id}/{amount}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String postPayment(@PathVariable(name="user_id") String user_id, @PathVariable(name="amount") Double amount) {
        Map<String,String> res=PaymentService.postPayment(user_id,amount);
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        result.put("credit", res.get("credit"));
        return result.toJSONString();
    }

    @PostMapping(value="/payment/cancel/{user_id}/{order_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String cancelPayment(@PathVariable(name="user_id") String user_id, @PathVariable(name="order_id") String order_id) {

        String res=PaymentService.cancelPayment(user_id,order_id);
        JSONObject result = new JSONObject();
        result.put("status", res);
        return result.toJSONString();
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

        Map<String,String> res=PaymentService.addFunds(user_id,amount);
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        result.put("credit", res.get("credit"));
        return result.toJSONString();
    }
    @PostMapping(value="/payment/create_user",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String createUser() {
        Map<String,String> res=PaymentService.createUser();
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        return result.toJSONString();
    }

    @GetMapping(value="/payment/find_user/{user_id}",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String findUser(@PathVariable(name="user_id") String user_id) {
        Map<String,String> res=PaymentService.findUser(user_id);
        JSONObject result = new JSONObject();
        result.put("user_id", res.get("user_id"));
        result.put("credit", res.get("credit"));
        return result.toJSONString();
    }
}
