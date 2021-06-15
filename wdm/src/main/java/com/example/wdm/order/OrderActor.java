package com.example.wdm.order;

import io.dapr.actors.ActorMethod;
import io.dapr.actors.ActorType;
import reactor.core.publisher.Mono;

/**
 * implementation of an OrderActor.
 */
@ActorType(name = "OrderActor")
public interface OrderActor {
    void registerReminder();

    // user_id as input
    @ActorMethod(returns = String.class)
    Mono<String> createOrder(String user_id);

    // order_id as input
    @ActorMethod(returns = String.class)
    Mono<String> removeOrder(String order_id);

    // order_id as input
    @ActorMethod(returns = String.class)
    Mono<String> findOrder();

    // order_id & item_id as input
    @ActorMethod(returns = String.class)
    Mono<String> addItem(String item_id); // order_id & item_id

    // order_id & item_id as input
    @ActorMethod(returns = String.class)
    Mono<String> removeItem(String item_id);

    // order_id & item_id as input
    @ActorMethod(returns = String.class)
    Mono<String> checkOut(String order_id);

    // order_id as input
    @ActorMethod(returns = String.class)
    Mono<String> set_status_false(String order_id);// order_id


}