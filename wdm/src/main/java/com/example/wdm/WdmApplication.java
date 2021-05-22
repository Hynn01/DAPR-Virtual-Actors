package com.example.wdm;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class WdmApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(WdmApplication.class, args);
//	}
//
//}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.wdm.state.StateClient;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import io.dapr.client.domain.TransactionalStateOperation;
import io.dapr.exceptions.DaprException;
import io.grpc.Status;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WdmApplication {

	public static void main(String[] args) {
		SpringApplication.run(WdmApplication.class, args);
	}
}
