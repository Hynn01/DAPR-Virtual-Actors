package com.example.wdm.payment;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PaymentService {

    public static Map<String, String> postPayment(String user_id, Integer amount) {
        String credit = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(user_id);
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new CallActor(actorId.toString(), actor, 3, amount));

            credit = future.get();

            System.out.println("Got user credit: "+credit);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String, String>();
        result.put("user_id",user_id);
        result.put("credit",credit);
        return result;
    }


    public static String cancelPayment() {


        return "'ok': (true/false)";
    }

    public static String getPaymentStatus() {

        return "'paid': (true/false)";
    }

    public static Map<String,String> addFunds(String user_id, Integer amount) {
        String credit = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(user_id);
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new CallActor(actorId.toString(), actor, 4, amount));

            credit = future.get();

            System.out.println("Got user credit: "+credit);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String,String>();
        result.put("user_id",user_id);
        result.put("credit",credit);
        return result;
    }

    public static Map<String,String> createUser() {
        String user_id = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
//            UUID uuid = UUID.randomUUID();
//            ActorId actorId = new ActorId(uuid.toString());
            ActorId actorId = ActorId.createRandom();
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new CallActor(actorId.toString(), actor, 1));

            user_id = future.get();

            System.out.println("Got user id:"+user_id);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String,String>();
        result.put("user_id",user_id);
        return result;
    }

    public static Map<String,String> findUser(@PathVariable(name="user_id") String user_id) {
        String credit = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(user_id);
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new CallActor(actorId.toString(), actor, 2));

            credit = future.get();

            System.out.println("Got user credit:"+credit);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        String json =  "{\"user_id\":"+user_id+","+"\"credit\":"+credit+"}";
        Map<String,String> result=new HashMap<String,String>();
        result.put("user_id",user_id);
        result.put("credit",credit);

        return result;
    }
}
