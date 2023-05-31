package com.francofral.jpastreams;

import com.speedment.jpastreamer.application.JPAStreamer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringJpastreamerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpastreamerDemoApplication.class, args);
    }

//    @Bean
//    public JPAStreamer jpaStreamer() {
//        return JPAStreamer.of("jpastreamdb");
//    }

}
