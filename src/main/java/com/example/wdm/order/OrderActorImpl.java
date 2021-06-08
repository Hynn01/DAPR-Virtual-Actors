package com.example.wdm.order;
import com.example.wdm.stock.StockActor;
import com.example.wdm.stock.StockService;
import io.dapr.actors.ActorId;
import io.dapr.actors.runtime.*;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprClientGrpc;
import io.dapr.utils.TypeRef;
import io.dapr.v1.DaprProtos;
import reactor.core.publisher.Mono;
import com.example.wdm.payment.PaymentService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class OrderActorImpl extends AbstractActor implements OrderActor, Remindable<Integer> {

    /**
     * Format to output date and time.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * This is the constructor of an actor implementation, while also registering a timer.
     * @param runtimeContext The runtime context object which contains objects such as the state provider.
     * @param id             The id of this actor.
     */
    public OrderActorImpl (ActorRuntimeContext runtimeContext, ActorId id) {
        super(runtimeContext, id);

//        super.registerActorTimer(
//                null,
//                "clock",
//                "ping!",
//                Duration.ofSeconds(2),
//                Duration.ofSeconds(1)).block();
    }

    /**
     * Registers a reminder.
     */

    @Override
    public void registerReminder() {
        super.registerReminder(
                "myremind",
                (int) (Integer.MAX_VALUE * Math.random()),
                Duration.ofSeconds(5),
                Duration.ofSeconds(2)).block();
    }

    @Override
    public Mono<String> create_order(String user_id) {
        String result = "";
        System.out.println("service:create order:"+this.getId());
        ArrayList<String> items = new ArrayList<String>();
        super.getActorStateManager().add("order_id", this.getId().toString()).block();
        super.getActorStateManager().add("paid", false).block();
        super.getActorStateManager().add("items", items).block();
        super.getActorStateManager().add("user_id", user_id).block();
        super.getActorStateManager().add("total_cost", 0.0d).block();
        result = this.getId().toString();
        this.unregisterReminder("myremind").block();
        return Mono.just(result);
    }

    @Override
    public Mono<String> remove_order(String order_id) {
        System.out.println("service delete order:");
        String result = "";
        String order_get = super.getActorStateManager().get("order_id", String.class).block();
        if(!order_get.equals(order_id)){
            result = "there is no this order";
        }
        else{
            super.getActorStateManager().set("order_id","null").block();
            super.getActorStateManager().set("paid","null").block();
            super.getActorStateManager().set("items","null").block();
            super.getActorStateManager().set("user_id","null").block();
            super.getActorStateManager().set("total_cost","null").block();
            result = "delete successful";
        }
        this.unregisterReminder("myremind").block();
        return Mono.just(result);
    }

    @Override
    public Mono<String> find_order() {
        System.out.println("service: find order");
        String result = "";
        String order_get = super.getActorStateManager().get("order_id", String.class).block();
        System.out.println(order_get);
        if(order_get.equals("null")){
            result = "there is no this order";
            System.out.println(result);
        }
        else {
            Boolean paid = super.getActorStateManager().get("paid", Boolean.class).block();
            ArrayList<String> items = super.getActorStateManager().get("items", ArrayList.class).block();
            String user_id = super.getActorStateManager().get("user_id", String.class).block();
            double total_cost = super.getActorStateManager().get("total_cost", double.class).block();
            result = paid.toString()+"#"+items.toString()+'#'+user_id+"#"+String.valueOf(total_cost);
        }
        this.unregisterReminder("myremind").block();
        return Mono.just(result);
    }

    @Override
    public Mono<String> add_item(String item_id) {
        System.out.println("service: add item");
        String result = "";
        String order_get = super.getActorStateManager().get("order_id", String.class).block();
        if(!order_get.equals(this.getId().toString())){
            result = "there is no this order";
        }
        else{
            StockService stockService = new StockService();
            Map<String, String> find_res = stockService.findItem(item_id);
            double cost = Double.parseDouble(find_res.get("price"));
            double totalCost = super.getActorStateManager().get("total_cost", Double.class).block();
            totalCost = totalCost + cost;
            super.getActorStateManager().set("total_cost", totalCost).block();
            ArrayList<String> items = super.getActorStateManager().get("items", ArrayList.class).block();
            items.add(item_id);
            super.getActorStateManager().set("items", items).block();
            result = "success";
        }
        this.unregisterReminder("myremind").block();
        return Mono.just(result);
    }

    @Override
    public Mono<String> remove_item(String item_id) {
        System.out.println("service: delete item");
        String result = "";
        String order_get = super.getActorStateManager().get("order_id", String.class).block();
        if(!order_get.equals(this.getId().toString())){
            result = "there is no this order";
        }
        else {
            StockService stockService = new StockService();
            Map<String, String> find_res = stockService.findItem(item_id);
            double cost = Double.parseDouble(find_res.get("price"));
            double totalCost = super.getActorStateManager().get("total_cost", Double.class).block();
            totalCost = totalCost - cost;
            super.getActorStateManager().set("total_cost", totalCost).block();
            ArrayList<String> items = super.getActorStateManager().get("items", ArrayList.class).block();
            items.remove(item_id);
            System.out.println(items);
            super.getActorStateManager().set("items", items).block();
            result = "success";
        }
        this.unregisterReminder("myremind").block();
        return Mono.just(result);
    }

    @Override
    public Mono<String> checkout(String order_id) {
        ArrayList<String> res = new ArrayList<>();
        String stockRes = "Sufficient stock";
        String paymentRes = "Enough credit";
        ArrayList<String> items = super.getActorStateManager().get("items", ArrayList.class).block();
        HashMap<String, Integer> item_set = new HashMap<>();
        for(String item: items){
            if(!item_set.containsKey(item)){item_set.put(item, 1);}
            else {
                Integer temp = item_set.get(item);
                item_set.put(item, temp + 1);
            }
        }
        PaymentService paymentService = new PaymentService();
        double total_cost = super.getActorStateManager().get("total_cost", Double.class).block();
        Map<String, String> tempMap = paymentService.postPayment(super.getActorStateManager().get("user_id", String.class).block(), total_cost);
        if(tempMap.get("credit").equals("-1")){paymentRes = "user don't hold enough credit";}
        else{
            String tempKey = "";
            StockService stockService = new StockService();
            for(String key: item_set.keySet()){
                tempMap = stockService.subtractStock(key, item_set.get(key));
                if(Integer.parseInt(tempMap.get("stock"))<0){
                    stockRes = "Insufficient stock";
                    tempKey = key;
                    break;
                }
            }
//        recover stock dataset if checkout fails
            if(stockRes.equals("Insufficient stock")){
                tempMap = paymentService.addFunds(super.getActorStateManager().get("user_id", String.class).block(), total_cost);
                for(String key: item_set.keySet()){
                    if(!key.equals(tempKey)){
                        tempMap = stockService.addStock(key, item_set.get(key));
                    }
                    else{
                        tempMap = stockService.addStock(tempKey, item_set.get(tempKey));
                        break;
                    }
                }
            }
        }
        this.unregisterReminder("myremind").block();
        return Mono.just(stockRes + paymentRes);
    }

    @Override
    public Mono<String> set_status(String order_id, String status) {
        if(status.equals("false")) {
            super.getActorStateManager().set("paid", false).block();
        }else{
            super.getActorStateManager().set("paid", true).block();
        }
        return Mono.just("success");
    }

    /**
     * Method used to determine reminder's state type.
     * @return Class for reminder's state.
     */
    @Override
    public TypeRef<Integer> getStateType() {
        return TypeRef.INT;
    }
//
//    @Override
//    public void clock(String message){
//        Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        String utcNowAsString = DATE_FORMAT.format(utcNow.getTime());
//        System.out.println("Server timer for actor "
//                + super.getId() + ": "
//                + (message == null ? "" : message + " @ " + utcNowAsString));
//    }

    /**
     * Method used be invoked for a reminder.
     * @param reminderName The name of reminder provided during registration.
     * @param state        The user state provided during registration.
     * @param dueTime      The invocation due time provided during registration.
     * @param period       The invocation period provided during registration.
     * @return Mono result.
     */
    @Override
    public Mono<Void> receiveReminder(String reminderName, Integer state, Duration dueTime, Duration period) {
        return Mono.fromRunnable(() -> {
            Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            String utcNowAsString = DATE_FORMAT.format(utcNow.getTime());

            String message = String.format("Server reminded actor %s of: %s for %d @ %s",
                    this.getId(), reminderName, state, utcNowAsString);
        });
    }

}
