package com.example.wdm.order;
import java.util.concurrent.Callable;

public class OrderCallActor implements Callable {
    private String actorId;
    private OrderActor actor;
    private Integer type;
    private Integer amount;
    private String user_id;
    private String order_id;
    private String item_id;
    private Integer change;
    public OrderCallActor(String actorId, OrderActor actor, Integer type){
        this.actorId = actorId;
        this.actor  = actor;
        this.type = type;
    }
    public OrderCallActor(String actorId, OrderActor actor, Integer type, String user_id){
        this.user_id = user_id;
        this.actorId = actorId;
        this.actor = actor;
        this.type = type;
    }
    public OrderCallActor(String actorId, OrderActor actor, String order_id, Integer type){
        this.order_id = order_id;
        this.actorId = actorId;
        this.actor = actor;
        this.type = type;
    }
    public OrderCallActor(String actorId, OrderActor actor, String item_id, Integer type, Integer change){
        this.item_id = item_id;
        this.actorId = actorId;
        this.actor = actor;
        this.type = type;
        this.change = change;
    }
    @Override
    public Object call() throws Exception {
        actor.registerReminder();
        String result = "";

        System.out.println("OrdercallActor");
        switch (type) {
            case 1: {
                result = actor.createOrder(user_id).block();
                break;
            }
            case 2:{
                result=actor.removeOrder(order_id).block();
                break;
            }
            case 3:{
                result = actor.findOrder().block().toString();
                break;
            }
            case 4:{
                result = actor.addItem(item_id).block().toString();
                break;
            }
            case 5:{
                result = actor.removeItem(item_id).block().toString();
                break;
            }
            case 6:{
                result = actor.checkOut(order_id).block().toString();
                break;
            }
            case 7: {
                result = actor.set_status_false(order_id).block().toString();
                break;
            }
        }
        return result;
    }
}