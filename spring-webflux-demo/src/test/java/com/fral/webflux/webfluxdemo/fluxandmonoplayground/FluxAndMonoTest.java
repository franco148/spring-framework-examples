package com.fral.webflux.webfluxdemo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class FluxAndMonoTest {

    @Test
    void fluxTestSuccess() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                                      .log();

        stringFlux.subscribe(System.out::println, (exception) -> System.err.println());
    }

    @Test
    void fluxTestError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                                      .concatWith(Flux.error(new RuntimeException("Exception occurred...")));

        stringFlux.subscribe(System.out::println, System.err::println);
    }

    @Test
    void fluxTestErrorVerbose() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred...")))
                .log();

        stringFlux.subscribe(System.out::println, System.err::println);
    }

    @Test
    void fluxTestAfterError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred...")))
                .concatWith(Flux.just("After Error"))
                .log();

        stringFlux.subscribe(System.out::println, System.err::println);
    }

    @Test
    void fluxTestWhenCompleted() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("Exception occurred...")))
                .concatWith(Flux.just("After Error"))
                .log();

        stringFlux.subscribe(System.out::println, System.err::println, ()-> System.out.println("Completed..."));
    }
}
