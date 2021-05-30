package com.example.wdm.payment;

import io.dapr.actors.client.ActorClient;
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

    @RequestMapping("/payment/pay/{user_id}/{order_id}/{amount}")
    public String postPayment() {

        return "/payment/pay/{user_id}/{order_id}/{amount}";
    }

    @RequestMapping("/payment/cancel/{user_id}/{order_id}")
    public String cancelPayment() {

        return "/payment/cancel/{user_id}/{order_id}";
    }

    @RequestMapping("/payment/status/{order_id}")
    public String index3() {
        return "/payment/status/{order_id}";
    }

    @RequestMapping("/payment/add_funds/{user_id}/{amount}")
    public String index4() {
        return "/payment/add_funds/{user_id}/{amount}";
    }

    @RequestMapping("/payment/create_user")
    public String createUser() {
        return "user_id";
    }

    @RequestMapping("/payment/find_user/{user_id}")
    public String index6() {
        return "/payment/find_user/{user_id}";
    }

}
