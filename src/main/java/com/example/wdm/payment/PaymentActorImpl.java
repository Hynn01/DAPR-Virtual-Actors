/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package com.example.wdm.payment;

import io.dapr.actors.ActorId;
import io.dapr.actors.runtime.AbstractActor;
import io.dapr.actors.runtime.ActorRuntime;
import io.dapr.actors.runtime.ActorRuntimeContext;
import io.dapr.actors.runtime.Remindable;
import io.dapr.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Implementation of the DemoActor for the server side.
 */
public class PaymentActorImpl extends AbstractActor implements PaymentActor, Remindable<Integer> {

  /**
   * Format to output date and time.
   */
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  /**
   * This is the constructor of an actor implementation, while also registering a timer.
   * @param runtimeContext The runtime context object which contains objects such as the state provider.
   * @param id             The id of this actor.
   */
  public PaymentActorImpl(ActorRuntimeContext runtimeContext, ActorId id) {
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
  public Mono<String> createUser() {
    System.out.println("service:create user:"+this.getId());
    super.getActorStateManager().set("credit", 0.0).block();
    this.unregisterReminder("myremind").block();
//    ActorRuntime.getInstance().deactivate("PaymentActor",this.getId().toString()).block();
    return Mono.just(this.getId().toString());
  }

  @Override
  public Mono<String> findUser() {
    System.out.println("service: find user");
    Double credit = super.getActorStateManager().get("credit", Double.class).block();
    this.unregisterReminder("myremind").block();
//    ActorRuntime.getInstance().deactivate("PaymentActor",this.getId().toString()).block();
    return  Mono.just(String.valueOf(credit));
  }

  @Override
  public Mono<String> postPayment(Double amount) {
    System.out.println("service:postPayment");
    Double credit_before = super.getActorStateManager().get("credit", Double.class).block();
    System.out.println("credit_before: "+credit_before);
    if(credit_before - amount>=0){
      super.getActorStateManager().set("credit", credit_before - amount).block();
      System.out.println("reduce");
    }
    Double credit_after = super.getActorStateManager().get("credit", Double.class).block();
    System.out.println("credit_after: " + credit_after);
    this.unregisterReminder("myremind").block();
//    ActorRuntime.getInstance().deactivate("PaymentActor",this.getId().toString()).block();
    if(credit_after < credit_before) {
      return Mono.just(String.valueOf(credit_after));
    }
    else {
      return Mono.just("-1");
    }
  }

  @Override
  public Mono<String> addFunds(Double amount) {
    System.out.println("service : add funds");
    Double c = super.getActorStateManager().get("credit", Double.class).block() + amount;
    super.getActorStateManager().set("credit", c).block();
    Double credit = super.getActorStateManager().get("credit", Double.class).block();
    this.unregisterReminder("myremind").block();
//    ActorRuntime.getInstance().deactivate("PaymentActor",this.getId().toString()).block();
    return Mono.just(String.valueOf(credit));
  }

  @Override
  public Mono<String> cancelPayment(String user_id, String order_id) {
    System.out.println("service:cancelPayment");

    return null;
  }

  @Override
  public Mono<String> getPaymentStatus(String order_id) {
    System.out.println("service:getPaymentStatus");

    return null;
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
