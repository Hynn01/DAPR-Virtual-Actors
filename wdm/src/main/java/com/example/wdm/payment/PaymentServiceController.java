package com.example.wdm.payment;

import io.dapr.actors.client.ActorClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PaymentServiceController {

    private static final int NUM_ACTORS = 3;

    @PostMapping("/payment/pay/{user_id}/{order_id}/{amount}")
    public String postPayment() {
        //substract money

        //if success, add a order record to the user; if fail, don't add; order id和amount先不关联

        return "true/fail";
    }

    @RequestMapping("/payment/cancel/{user_id}/{order_id}")
    public String cancelPayment() {


        return "'ok': (true/false)";
    }

    @RequestMapping("/payment/status/{order_id}")
    public String getPaymentStatus() {

        return "'paid': (true/false)";
    }

    @RequestMapping("/payment/add_funds/{user_id}/{amount}")
    public String addFunds() {
        return "done(true/false)";
    }

    @RequestMapping("/payment/create_user")
    public String createUser() {
        return "user_id";
    }

    @RequestMapping("/payment/find_user/{user_id}")
    public String findUser() {
        return "{userid:'',usercredir:''}";
    }

}
