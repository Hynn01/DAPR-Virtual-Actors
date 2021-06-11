package com.example.wdm.stock;

import com.example.wdm.stock.StockActor;
import io.dapr.actors.ActorId;
import io.dapr.actors.runtime.AbstractActor;
import io.dapr.actors.runtime.ActorRuntimeContext;
import io.dapr.actors.runtime.Remindable;
import io.dapr.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.TimeZone;

public class StockActorImpl extends AbstractActor implements StockActor, Remindable<Integer> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * This is the constructor of an actor implementation, while also registering a timer.
     * @param runtimeContext The runtime context object which contains objects such as the state provider.
     * @param id             The id of this actor.
     */
    public StockActorImpl(ActorRuntimeContext runtimeContext, ActorId id) {
        super(runtimeContext, id);
    }
    @Override
    public void registerReminder() {
        super.registerReminder(
                "myremind",
                (int) (Integer.MAX_VALUE * Math.random()),
                Duration.ofSeconds(5),
                Duration.ofSeconds(2)).block();
    }

    @Override
    public Mono<String> findItem() {
        System.out.println("service: find item");
        double price = super.getActorStateManager().get("price", Double.class).block();
        Integer stock = super.getActorStateManager().get("stock", Integer.class).block();
        String res = stock.toString() + "#" + price;
        this.unregisterReminder("myremind").block();
        return Mono.just(res);
    }

    @Override
    public Mono<String> subtractItem(Integer stock) {
        System.out.println("service: subtract item");
        int lastStock = super.getActorStateManager().get("stock", Integer.class).block();
        stock = lastStock - stock;
        super.getActorStateManager().set("stock", stock).block();
        this.unregisterReminder("myremind").block();
        return Mono.just(stock.toString());
    }

    @Override
    public Mono<String> addItem(Integer stock) {
        System.out.println("service: add item");
        int lastStock = super.getActorStateManager().get("stock", Integer.class).block();
        stock = lastStock + stock;
        super.getActorStateManager().set("stock", stock).block();
        this.unregisterReminder("myremind").block();
        return Mono.just(stock.toString());
    }

    @Override
    public Mono<String> createItem(double price) {
        System.out.println("service:create item:"+this.getId());
        super.getActorStateManager().set("price", price).block();
        super.getActorStateManager().set("stock", 0).block();
        this.unregisterReminder("myremind").block();
//    ActorRuntime.getInstance().deactivate("PaymentActor",this.getId().toString()).block();
        return Mono.just(this.getId().toString());
    }

    @Override
    public TypeRef<Integer> getStateType() {
        return TypeRef.INT;
    }

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
