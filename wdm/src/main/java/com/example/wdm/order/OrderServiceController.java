package com.example.wdm.order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderServiceController {

    @RequestMapping("/orders/create/{user_id}")
    public String index1() {
        return "/orders/create/{user_id}";
    }

    @RequestMapping("/orders/remove/{order_id}")
    public String index2() {
        return "/orders/remove/{order_id}";
    }

    @RequestMapping("/orders/find/{order_id}")
    public String index3() {
        return "/orders/find/{order_id}";
    }

    @RequestMapping("/orders/addItem/{order_id}/{item_id}")
    public String index4() {
        return "/orders/addItem/{order_id}/{item_id}";
    }

    @RequestMapping("/orders/removeItem/{order_id}/{item_id}")
    public String index5() {
        return "/orders/removeItem/{order_id}/{item_id}";
    }

    @RequestMapping("/orders/checkout/{order_id}")
    public String index6() {
        return "/orders/checkout/{order_id}";
    }

}
