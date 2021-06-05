package com.example.wdm.stock;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.HashMap;
import java.util.Map;

public class StockService{

    public Map<String, String> findItem(String item_id) {
        String stock = "";
        String price = "";
        String result = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            ActorId actorId = new ActorId(item_id);
            StockActor actor = builder.build(actorId);
            Future<String> future = threadPool.submit(new StockCallActor(actorId.toString(), actor, 2));

            result = future.get();
            String [] arr = result.split("#");
            stock = arr[1];
            price = arr[0];

            System.out.println("stock:"+stock+"\n price:"+price);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> res =new HashMap<String, String>();
        res.put("stock",stock);
        res.put("price",price);
        return res;
    }

    public Map<String, String> subtractStock(String item_id, Integer number) {
        String stock = "";
        String result = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(item_id);
            StockActor actor = builder.build(actorId);
            Future<String> future = threadPool.submit(new StockCallActor(actorId.toString(), actor, 3, number));
            result = future.get();
            String [] arr = result.split("#");
            stock = arr[1];
            System.out.println("Subtract - controller, done");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> res =new HashMap<String, String>();
        res.put("stock",stock);
        res.put("item_id",item_id);
        return res;
    }

    public Map<String, String> addStock(String item_id, Integer number) {
        String stock = "";
        String result = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();

            ActorId actorId = new ActorId(item_id);
            StockActor actor = builder.build(actorId);
            Future<String> future = threadPool.submit(new StockCallActor(actorId.toString(), actor, 4, number));
            result = future.get();
            String [] arr = result.split("#");
            stock = arr[1];
            System.out.println("Add - controller, done");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,String> res =new HashMap<String, String>();
        res.put("stock",stock);
        res.put("item_id",item_id);
        return res;
    }

    public String createItem(Double price) {
        String item_id = "";
        try (ActorClient client = new ActorClient()) {
            ActorProxyBuilder<StockActor> builder = new ActorProxyBuilder(StockActor.class, client);
//            List<Thread> threads = new ArrayList<>(NUM_ACTORS);
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
//            UUID uuid = UUID.randomUUID();
//            ActorId actorId = new ActorId(uuid.toString());
            ActorId actorId = ActorId.createRandom();
            StockActor actor = builder.build(actorId);

            Future<String> future = threadPool.submit(new StockCallActor(actorId.toString(), actor, 1, price));

            item_id = future.get();

            System.out.println("Got item id:"+item_id);
            System.out.println("price: "+ price);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        String json =  "{\"item_id\":"+item_id+"}";
        return json;
    }
}