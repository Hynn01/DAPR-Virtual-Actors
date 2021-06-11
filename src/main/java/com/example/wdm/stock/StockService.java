package com.example.wdm.stock;

import com.example.wdm.stock.StockActor;
import com.example.wdm.stock.StockCallActor;
import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;
import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StockService {
    public static Map<String, String> findItem(String item_id) {
        String res = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = new ActorId(item_id);
            StockActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new StockCallActor(actorId.toString(), actor, 1));
            res = future.get();


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        String[] arr = res.split("#");
        Map<String,String> result=new HashMap<String, String>();
        result.put("item_id", item_id);
        result.put("stock", arr[0]);
        result.put("price", arr[1]);
        return result;
    }


    public static Map<String, String> subtractItem(String item_id, Integer stock) {
        String res = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(item_id);
            StockActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new StockCallActor(actorId.toString(), actor, stock, 2));

            res = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> result=new HashMap<String, String>();
        result.put("item_id", item_id);
        result.put("stock", res);
        return result;
    }



    public static Map<String, String> addItem(String item_id, Integer stock) {
        String res = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = new ActorId(item_id);
            StockActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new StockCallActor(actorId.toString(), actor, stock, 3));

            res = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> result=new HashMap<String, String>();
        result.put("item_id", item_id);
        result.put("stock", res);
        return result;
    }

    public static Map<String, String> createItem(double price) {
        String res = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = ActorId.createRandom();
            StockActor actor = builder.build(actorId);
            Future<String> future =
                    threadPool.submit(new StockCallActor(actorId.toString(), actor, price, 4));

            res = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> result=new HashMap<String, String>();
        result.put("item_id", res);
        System.out.println(result.get("item_id"));
        return result;
    }
}
