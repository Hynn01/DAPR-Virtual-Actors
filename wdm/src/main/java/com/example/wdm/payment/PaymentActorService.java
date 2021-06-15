package com.example.wdm.payment;

import com.example.wdm.DaprApplication;
import io.dapr.actors.runtime.ActorRuntime;
import java.time.Duration;

/**
 * Service for Actor runtime.
 */
public class PaymentActorService {

  /**
   * The main method of this app.
   * @param args Input arguments.
   * @throws Exception An Exception.
   */
  public static void main(String[] args) throws Exception {

    final int port = 3002;

    // Idle timeout until actor instance is deactivated.
    ActorRuntime.getInstance().getConfig().setActorIdleTimeout(Duration.ofSeconds(30));
    // How often actor instances are scanned for deactivation and balance.
    ActorRuntime.getInstance().getConfig().setActorScanInterval(Duration.ofSeconds(10));
    // How long to wait until for draining an ongoing API call for an actor instance.
    ActorRuntime.getInstance().getConfig().setDrainOngoingCallTimeout(Duration.ofSeconds(10));
    // Determines whether to drain API calls for actors instances being balanced.
    ActorRuntime.getInstance().getConfig().setDrainBalancedActors(true);

    // Register the Actor class.
    ActorRuntime.getInstance().registerActor(PaymentActorImpl.class);

    // Start Dapr's callback endpoint.
    DaprApplication.start(port);
  }
}
