/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package com.example.wdm.stock;

import io.dapr.actors.ActorId;
import io.dapr.actors.runtime.AbstractActor;
import io.dapr.actors.runtime.ActorRuntimeContext;
import io.dapr.actors.runtime.Remindable;
import io.dapr.utils.TypeRef;
import reactor.core.publisher.Mono;
import io.dapr.actors.runtime.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.NoSuchElementException;

/**
 * Implementation of the DemoActor for the server side.
 */
public class StockActorImpl extends AbstractActor implements StockActor, Remindable<Integer> {

  /**
   * Format to output date and time.
   */
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  /**
   * This is the constructor of an actor implementation, while also registering a timer.
   * @param runtimeContext The runtime context object which contains objects such as the state provider.
   * @param id             The id of this actor.
   */
  public StockActorImpl(ActorRuntimeContext runtimeContext, ActorId id) {
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


  @Override
  public Mono<String> createItem(Double price) {
    System.out.println("service:create item:"+this.getId());
    super.getActorStateManager().add("price", price).block();
    super.getActorStateManager().add("stock", 0).block();
//    super.getActorStateManager().add("id", this.getId().toString()).block();
    this.unregisterReminder("myremind").block();
    return Mono.just(this.getId().toString());
  }

  @Override
  public Mono<String> findItem() {
        Double price = super.getActorStateManager().get("price", Double.class).block();
        System.out.println(price);
        Integer stock = super.getActorStateManager().get("stock", Integer.class).block();
        String result = price.toString()+"#"+stock.toString();
        this.unregisterReminder("myremind").block();
        return Mono.just(result);
  }

  @Override
  public Mono<String> subtractStock(Integer number) {
    int stock = super.getActorStateManager().get("stock", int.class).block();
        int newStock = stock - number;
        //super.getActorStateManager().set("stock", newStock).block();
        System.out.println("new credit: "+newStock);
        super.getActorStateManager().set("stock", newStock).block();
        this.unregisterReminder("myremind").block();
        //ActorRuntime.getInstance().deactivate("StockActor",this.getId().toString()).block();
        return Mono.just(Integer.toString(newStock));

  }

  @Override
  public Mono<String> addStock(Integer number) {
      int stock = super.getActorStateManager().get("stock", int.class).block();
      int newStock = stock + number;
        //super.getActorStateManager().set("stock", newStock).block();
      System.out.println("new credit: "+newStock);
      super.getActorStateManager().set("stock", newStock).block();
      this.unregisterReminder("myremind").block();
      return Mono.just(Integer.toString(newStock));
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

      // Handles the request by printing message.
//      System.out.println(message);
    });
  }

}
