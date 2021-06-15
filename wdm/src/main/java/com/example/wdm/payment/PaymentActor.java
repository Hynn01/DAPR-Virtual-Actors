package com.example.wdm.payment;

import io.dapr.actors.ActorMethod;
import io.dapr.actors.ActorType;
import reactor.core.publisher.Mono;

/**
 * implementation of an PaymentActor.
 */
@ActorType(name = "PaymentActor")
public interface PaymentActor {

  void registerReminder();

  @ActorMethod(returns = String.class)
  Mono<String> createUser();

  @ActorMethod(returns = String.class)
  Mono<String> findUser();

  @ActorMethod(returns = String.class)
  Mono<String> postPayment(Double amount);

  @ActorMethod(returns = String.class)
  Mono<String> addFunds(Double amount);

}
