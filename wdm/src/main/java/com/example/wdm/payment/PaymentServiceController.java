package com.example.wdm.payment;

import io.dapr.actors.client.ActorClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class PaymentServiceController {

    private static final int NUM_ACTORS = 3;

    @PostMapping("/payment/pay/{user_id}/{order_id}/{amount}")
    public String postPayment() {
        //substract money

        //if success, add a order record to the user; if fail, don't add; order id和amount先不关联

        return "true/fail";
    }

    @RequestMapping("/payment/cancel/{user_id}/{order_id}")
    public String cancelPayment() {


        return "'ok': (true/false)";
    }

    @RequestMapping("/payment/status/{order_id}")
    public String getPaymentStatus() {

        return "'paid': (true/false)";
    }

    @RequestMapping("/payment/add_funds/{user_id}/{amount}")
    public String addFunds() {
        return "done(true/false)";
    }

    @RequestMapping("/payment/create_user")
    public String createUser() {
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = ActorId.createRandom();
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new CallActor(actorId.toString(), actor, 1));

            String user_id = future.get();

            System.out.println("Got user id:"+user_id);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return "user_id";
    }

    @RequestMapping("/payment/find_user/{user_id}")
    public String findUser() {
        return "{userid:'',usercredir:''}";
    }

}
