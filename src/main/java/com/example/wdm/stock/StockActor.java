package com.example.wdm.stock;

import io.dapr.actors.ActorMethod;
import io.dapr.actors.ActorType;
import reactor.core.publisher.Mono;

@ActorType(name = "StockActor")
public interface StockActor {


    void registerReminder();

    @ActorMethod(returns = String.class)
    Mono<String> findItem();

    @ActorMethod(returns = String.class)
    Mono<String> subtractItem(Integer stock);

    @ActorMethod(returns = String.class)
    Mono<String> addItem(Integer stock);

    @ActorMethod(returns = String.class)
    Mono<String> createItem(double price);


}
