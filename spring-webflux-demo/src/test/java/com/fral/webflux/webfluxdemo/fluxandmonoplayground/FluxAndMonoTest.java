package com.fral.webflux.webfluxdemo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    @Test
    void fluxTestElements_WithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                    .expectNext("Spring")
                    .expectNext("Spring Boot")
                    .expectNext("Reactive Spring")
                    .verifyComplete();
    }

    @Test
    void fluxTestElements_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred...")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
//                .verifyError();
//                .expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred...")
                .verify();
    }

    @Test
    void fluxTestElementsCount_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred...")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
//                .verifyError();
//                .expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred...")
                .verify();
    }

    @Test
    void fluxTestElements_WithError2() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred...")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
//                .verifyError();
//                .expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred...")
                .verify();
    }

    @Test
    public void monoTest(){

        Mono<String> stringMono = Mono.just("Spring");

        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();

    }

    @Test
    public void monoTest_Error(){

        StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException.class)
                .verify();

    }
}
