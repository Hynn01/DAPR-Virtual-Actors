package com.example.wdm.order;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderService {

    public Map<String,String> createOrderService(String user_id){
        String order_id = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<OrderActor> builder = new ActorProxyBuilder(OrderActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = ActorId.createRandom();
            OrderActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new OrderCallActor(actorId.toString(), actor, 1, user_id));
            order_id = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String,String> result=new HashMap<String,String>();
        result.put("order_id",order_id);
        return result;

//        String json = "{\"order_id\":" + order_id + "}";
//        return json;
    }

    public Map<String,String> removeOrderService(String order_id){
        String result = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<OrderActor> builder = new ActorProxyBuilder(OrderActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = new ActorId(order_id);
            OrderActor actor = builder.build(actorId);
            Future<String> future = threadPool.submit(new OrderCallActor(actorId.toString(), actor, order_id,2));
            result = future.get();
            System.out.println("Got user credit:" + result);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
//        return result;
        Map<String,String> mapResult=new HashMap<String,String>();
        mapResult.put("result",result);
        return mapResult;
    }

    public Map<String,String> findOrderService(String order_id){
        String result = "";
        String paid = "";
        String items = "";
        String user_id = "";
        String total_cost = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<OrderActor> builder = new ActorProxyBuilder(OrderActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(order_id);
            OrderActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new OrderCallActor(actorId.toString(), actor, 3));
            result = future.get();
            String[] arr = result.split("#");
            paid = arr[0];
            items = arr[1];
            user_id = arr[2];
            total_cost = arr[3];
            System.out.println("Got user paid: " + paid);
            System.out.println("Got order items: " + items);
            System.out.println("Got user id " + user_id);
            System.out.println("Got user total_cost" + total_cost);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
//        String json = "{\"order_id\":" + order_id + "," + "\"paid\":" + paid + "\"items\":"
//                + items + "\"user_id\":" + user_id + "\"total_cost\":" + total_cost + "}";
//        return json;
        Map<String,String> mapResult=new HashMap<String,String>();
        mapResult.put("order_id",order_id);
        mapResult.put("paid",paid);
        mapResult.put("items",items);
        mapResult.put("user_id",user_id);
        mapResult.put("total_cost",total_cost);
        return mapResult;
    }

    public Map<String,String> addOrderService(String order_id, String item_id){
        String result = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<OrderActor> builder = new ActorProxyBuilder(OrderActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(order_id);
            OrderActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new OrderCallActor(actorId.toString(), actor, item_id, 4, 4));
            result = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> mapResult=new HashMap<String,String>();
        mapResult.put("result",result);
        return mapResult;
    }

    public Map<String,String> removeOrderService(String order_id, String item_id){
        String result = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<OrderActor> builder = new ActorProxyBuilder(OrderActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(order_id);
            OrderActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new OrderCallActor(actorId.toString(), actor, item_id, 5, 4));
            result = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> mapResult=new HashMap<String,String>();
        mapResult.put("result",result);
        return mapResult;
    }
}
