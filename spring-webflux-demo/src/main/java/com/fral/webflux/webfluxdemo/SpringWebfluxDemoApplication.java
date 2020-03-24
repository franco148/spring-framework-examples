package com.fral.webflux.webfluxdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * gradle build -x test : Ignore the tests.
 * java -jar -Dspring.profiles.active=<profile> <jar-file-path>
 */
@SpringBootApplication
public class SpringWebfluxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxDemoApplication.class, args);
    }

}
