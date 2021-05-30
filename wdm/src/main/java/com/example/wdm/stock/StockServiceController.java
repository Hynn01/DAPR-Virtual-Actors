package com.example.wdm.stock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockServiceController {

    @RequestMapping("/stock/find/{item_id}")
    public String index1() {
        return "/stock/find/{item_id}";
    }

    @RequestMapping("/stock/subtract/{item_id}/{number}")
    public String index2() {
        return "/stock/subtract/{item_id}/{number}";
    }

    @RequestMapping("/stock/add/{item_id}/{number}")
    public String index3() {
        return "/stock/add/{item_id}/{number}";
    }

    @RequestMapping("/stock/item/create/{price}")
    public String index4() {
        return "/stock/item/create/{price}";
    }
}
