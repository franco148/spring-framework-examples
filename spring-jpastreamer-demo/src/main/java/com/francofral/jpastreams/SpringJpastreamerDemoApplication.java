package com.francofral.jpastreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.francofral.jpastreams", "com.speedment.jpastreamer"})
//@SpringBootApplication
public class SpringJpastreamerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpastreamerDemoApplication.class, args);
    }

}
