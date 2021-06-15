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

    /**
     * Use user id to create an  order
     * @return order id / the same as orderactor id
     */
    @Override
    public Mono<String> createOrder(String user_id) {
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

    /**
     * delete a order
     * @return success or not.
     */
    @Override
    public Mono<String> removeOrder(String order_id) {
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

    /**
     * Find order's information
     * @return order's order id, payment status, items, user id, total cost.
     */
    @Override
    public Mono<String> findOrder() {
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

    /**
     * Add a specific item to the order
     * @param item_id
     * @return status
     */
    @Override
    public Mono<String> addItem(String item_id) {
        System.out.println("service: add item");
        String result = "";
        String order_get = super.getActorStateManager().get("order_id", String.class).block();
        if(!order_get.equals(this.getId().toString())){
            result = "there is no this order";
        }
        else{
            System.out.println("item_add"+item_id);
            Map<String, String> find_user = PaymentService.findUser(super.getActorStateManager().get("user_id",String.class).block());
            System.out.println(find_user.get("credit"));
            Map<String, String> find_res = StockService.findItem(item_id);
            double cost = Double.parseDouble(find_res.get("price"));
//            double cost = 1.0;
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

    /**
     * Remove a soecific item of a order
     * @param item_id
     * @return status
     */
    @Override
    public Mono<String> removeItem(String item_id) {
        System.out.println("service: delete item");
        String result = "";
        String order_get = super.getActorStateManager().get("order_id", String.class).block();
        if(!order_get.equals(this.getId().toString())){
            result = "there is no this order";
        }
        else {
            Map<String, String> find_res = StockService.findItem(item_id);
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

    /**
     * Checkour the order, if checkout not success. then add the stock back and add the credit back
     * @param order_id
     * @return status
     */
    @Override
    public Mono<String> checkOut(String order_id) {
        System.out.println("3 start checkout with " + super.getActorStateManager().get("order_id", String.class).block());
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
        double total_cost = super.getActorStateManager().get("total_cost", Double.class).block();
        Map<String, String> tempMap = PaymentService.postPayment(super.getActorStateManager().get("user_id", String.class).block(), total_cost);
        System.out.println("order:" + order_id + "belong to user:" + super.getActorStateManager().get("user_id", String.class).block() + "has totalcost of"+ total_cost);
        if(tempMap.get("credit").equals("-1")){paymentRes = "user don't hold enough credit";}
        else{
            String tempKey = "";
            for(String key: item_set.keySet()){
                System.out.println("item"+key);
                tempMap = StockService.subtractItem(key, item_set.get(key));
                if(Integer.parseInt(tempMap.get("stock"))<0){
                    stockRes = "Insufficient stock";
                    tempKey = key;
                    break;
                }
            }
//        recover stock dataset if checkout fails
            if(stockRes.equals("Insufficient stock")){
                tempMap = PaymentService.addFunds(super.getActorStateManager().get("user_id", String.class).block(), total_cost);
                for(String key: item_set.keySet()){
                    if(!key.equals(tempKey)){
                        tempMap = StockService.addItem(key, item_set.get(key));
                    }
                    else{
                        tempMap = StockService.addItem(tempKey, item_set.get(tempKey));
                        break;
                    }
                }
            }
        }
        if((stockRes + paymentRes).equals("Sufficient stockEnough credit")){super.getActorStateManager().set("paid", true).block();}
        this.unregisterReminder("myremind").block();
        return Mono.just(stockRes + paymentRes);
    }

    @Override
    public Mono<String> set_status_false(String order_id) {
        super.getActorStateManager().set("paid", false).block();
        this.unregisterReminder("myremind").block();
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
