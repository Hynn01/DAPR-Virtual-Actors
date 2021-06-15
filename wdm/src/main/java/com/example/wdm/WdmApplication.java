package com.example.wdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.reflect.Method;
import java.util.Arrays;

@SpringBootApplication
public class WdmApplication {

    public static void main(String[] args) throws Exception {
        String[] arguments;
        if (args.length < 1) {
            SpringApplication.run(WdmApplication.class, args);
            System.out.println("SpringApplication run");
//            throw new IllegalArgumentException("Requires at least one argument - name of the main class");
        } else {

            arguments = Arrays.copyOfRange(args, 1, args.length);
            Class mainClass = Class.forName(args[0]);
            Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            Object[] methodArgs = new Object[1];
            methodArgs[0] = arguments;
            mainMethod.invoke(mainClass, methodArgs);
        }
    }

}