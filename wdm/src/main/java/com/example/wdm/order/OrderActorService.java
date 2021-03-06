package com.example.wdm.order;

import com.example.wdm.DaprApplication;
import com.example.wdm.payment.PaymentActorImpl;
import io.dapr.actors.runtime.ActorRuntime;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.time.Duration;

/**
 * Service for Actor runtime.
 * 1. Build and install jars:
 * mvn clean install
 * 2. cd to [repo-root]/examples
 * 3. Run the server:
 * dapr run --components-path ./components/actors --app-id demoactorservice --app-port 3000 \
 *   -- java -jar target/dapr-java-sdk-examples-exec.jar io.dapr.examples.actors.DemoActorService -p 3000
 */
public class OrderActorService {

    /**
     * The main method of this app.
     * @param args The port the app will listen on.
     * @throws Exception An Exception.
     */
    public static void main(String[] args) throws Exception {

        final int port = 3000;

        // Idle timeout until actor instance is deactivated.
        ActorRuntime.getInstance().getConfig().setActorIdleTimeout(Duration.ofSeconds(30));
        // How often actor instances are scanned for deactivation and balance.
        ActorRuntime.getInstance().getConfig().setActorScanInterval(Duration.ofSeconds(10));
        // How long to wait until for draining an ongoing API call for an actor instance.
        ActorRuntime.getInstance().getConfig().setDrainOngoingCallTimeout(Duration.ofSeconds(10));
        // Determines whether to drain API calls for actors instances being balanced.
        ActorRuntime.getInstance().getConfig().setDrainBalancedActors(true);

        // Register the Actor class.
        ActorRuntime.getInstance().registerActor(OrderActorImpl.class);

        // Start Dapr's callback endpoint.
        DaprApplication.start(port);
    }
}
