package com.example.wdm.payment;

import com.example.wdm.order.OrderService;
import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PaymentService {

    /**
     * subtracts the amount of the order from the user’s credit (returns -1 if credit is not enough)
     * @param user_id
     * @param amount
     */
    public static Map<String, String> postPayment(String user_id, Double amount) {

        String credit = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(user_id);
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new PaymentCallActor(actorId.toString(), actor, 3, amount));
            credit = future.get();

//            System.out.println("Got user credit: "+credit);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String, String>();
        result.put("user_id",user_id);
        result.put("credit",credit);
        return result;
    }


    /**
     * cancels payment made by a specific user for a specific order.
     * @param user_id
     * @param order_id
     */
    public static String cancelPayment(String user_id, String order_id) {

        Map<String,String> orderResult=OrderService.findOrderService(order_id);

        if(orderResult.get("paid").equals("true")){
            Double amount=Double.valueOf(orderResult.get("total_cost"));
            addFunds(user_id, amount);
            OrderService.setOrderStatusFalseService(order_id);
            return "success";
        }else{
            return "success";
        }

    }

    /**
     * get payment status
     * @param order_id
     * @return the status of the payment (paid or not)
     */
    public static String getPaymentStatus(String order_id) {

        Map<String,String> orderResult=OrderService.findOrderService(order_id);

        return orderResult.get("paid");
    }

    /**
     * creates a user with 0 credit
     * @param user_id
     * @param amount
     * @return “user_id” - the user’s id
     */
    public static Map<String,String> addFunds(String user_id, Double amount) {
        String credit = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(user_id);
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new PaymentCallActor(actorId.toString(), actor, 4, amount));

            credit = future.get();

//            System.out.println("Got user credit: "+credit);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String,String>();
        result.put("user_id",user_id);
        result.put("credit",credit);
        return result;
    }

    /**
     *  returns the user information
     * @return “user_id” - the user’s id; “credit” - the user’s credit
     */
    public static Map<String,String> createUser() {
        String user_id = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<PaymentActor> builder = new ActorProxyBuilder(PaymentActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = ActorId.createRandom();
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new PaymentCallActor(actorId.toString(), actor, 1));

            user_id = future.get();
//            System.out.println("Got user id:"+user_id);

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
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(user_id);
            PaymentActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new PaymentCallActor(actorId.toString(), actor, 2));

            credit = future.get();

//            System.out.println("Got user credit:"+credit);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String,String>();
        result.put("user_id",user_id);
        result.put("credit",credit);

        return result;
    }
}
