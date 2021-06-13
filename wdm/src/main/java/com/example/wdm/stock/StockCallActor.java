package com.example.wdm.stock;


import com.example.wdm.payment.PaymentActor;

import java.util.concurrent.Callable;
public class StockCallActor implements Callable{
    private String item_id;
    private StockActor actor;
    private Integer type;
    private Double price;
    private Integer stock;
    public StockCallActor(String item_id, StockActor actor, Integer type){
        this.item_id = item_id;
        this.actor  = actor;
        this.type = type;
    }
    public StockCallActor(String item_id, StockActor actor, Double price, Integer type){
        this.item_id = item_id;
        this.actor = actor;
        this.type = type;
        this.price = price;
    }
    public StockCallActor(String item_id, StockActor actor, Integer stock, Integer type){
        this.item_id = item_id;
        this.actor = actor;
        this.type = type;
        this.stock = stock;
    }
    @Override
    public Object call() throws Exception {
        actor.registerReminder();
        String result = "";
        System.out.println("callActor");
        switch (type) {
            case 1: {
                result = actor.findItem().block().toString();
                break;
            }
            case 2:{
                result = actor.subtractItem(stock).block().toString();
                break;
            }
            case 3:{
                result = actor.addItem(stock).block().toString();
                break;
            }
            case 4:{
                result = actor.createItem(price).block().toString();
                break;
            }
        }
        return result;
    }
}
