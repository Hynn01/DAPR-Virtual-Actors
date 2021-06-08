package com.example.wdm.payment;

import com.sun.jdi.IntegerType;

import java.util.concurrent.Callable;

public class PaymentCallActor implements Callable {
    private String actorId;
    private PaymentActor actor;
    private Integer type;
    private Double amount;
    public PaymentCallActor(String actorId, PaymentActor actor, Integer type){
        this.actorId = actorId;
        this.actor  = actor;
        this.type = type;
    }
    public PaymentCallActor(String actorId, PaymentActor actor, Integer type, Double amount){
        this.actorId = actorId;
        this.actor = actor;
        this.type = type;
        this.amount = amount;
    }
    @Override
    public Object call() throws Exception {
        actor.registerReminder();
        String result = "";

        System.out.println("callActor");
        switch (type) {
            case 1: {
                result = actor.createUser().block();
                break;
            }
            case 2:{
                result = actor.findUser().block().toString();
                break;
            }
            case 3:{
                result = actor.postPayment(amount).block().toString();
                break;
            }
            case 4:{
                result = actor.addFunds(amount).block().toString();
                break;
            }
        }
        return result;
    }
}
