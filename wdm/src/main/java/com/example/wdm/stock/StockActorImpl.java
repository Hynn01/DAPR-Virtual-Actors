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

/**
 Implementation of the StockActor for the server side.
 **/
public class StockActorImpl extends AbstractActor implements StockActor, Remindable<Integer> {

    /**
     * Format to output date and time.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * This is the constructor of an actor implementation, while also registering a timer.
     * @param runtimeContext The runtime context object which contains objects such as the state provider.
     * @param id             The id of this actor.
     */
    public StockActorImpl(ActorRuntimeContext runtimeContext, ActorId id) {
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

    /**
     * Find the price and stock of an item
     * @return price, stock
     */
    @Override
    public Mono<String> findItem() {
        System.out.println("service: find item");
        double price = super.getActorStateManager().get("price", Double.class).block();
        Integer stock = super.getActorStateManager().get("stock", Integer.class).block();
        String res = stock.toString() + "#" + price;
        this.unregisterReminder("myremind").block();
        return Mono.just(res);
    }

    /**
     * Subtracts an item from stock by the amount specified.
     * @param stock the amount of stock to be subtracted
     * @return the latest stock after subtraction
     */
    @Override
    public Mono<String> subtractItem(Integer stock) {
        System.out.println("service: subtract item");
        int lastStock = super.getActorStateManager().get("stock", Integer.class).block();
        stock = lastStock - stock;
        super.getActorStateManager().set("stock", stock).block();
        this.unregisterReminder("myremind").block();
        return Mono.just(stock.toString());
    }

    /**
     * Adds an item from stock by the amount specified.
     * @param stock the amount of stock to be added
     * @return the latest stock after addition
     */
    @Override
    public Mono<String> addItem(Integer stock) {
        System.out.println("service: add item");
        int lastStock = super.getActorStateManager().get("stock", Integer.class).block();
        stock = lastStock + stock;
        super.getActorStateManager().set("stock", stock).block();
        this.unregisterReminder("myremind").block();
        return Mono.just(stock.toString());
    }

    /**
     * Create an item with its price, and stock is set to 0 by default.
     * @param price the price of the item
     * @return item id
     */
    @Override
    public Mono<String> createItem(double price) {
        System.out.println("service:create item:"+this.getId());
        super.getActorStateManager().set("price", price).block();
        super.getActorStateManager().set("stock", 0).block();
        this.unregisterReminder("myremind").block();
//    ActorRuntime.getInstance().deactivate("PaymentActor",this.getId().toString()).block();
        return Mono.just(this.getId().toString());
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
            // System.out.println(message);
        });
    }
}
