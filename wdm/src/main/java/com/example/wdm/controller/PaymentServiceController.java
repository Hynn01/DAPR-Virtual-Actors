package com.example.wdm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentServiceController {

    @RequestMapping("/payment/pay/{user_id}/{order_id}/{amount}")
    public String index1() {
        return "/payment/pay/{user_id}/{order_id}/{amount}";
    }

    @RequestMapping("/payment/cancel/{user_id}/{order_id}")
    public String index2() {
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
}
