package com.fral.webflux.webfluxdemo.repository;

import com.fral.webflux.webfluxdemo.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(new Item(null,"Samsung TV",400.0),
            new Item(null,"LG TV",420.0),
            new Item(null,"Apple Watch",299.99),
            new Item(null,"Beats Headphones",149.99),
            new Item("ABC","Bose Headphones",149.99));

    @Before
    public void setUp(){

        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted Item is :" + item);
                }))
                .blockLast();

    }


    @Test
    public void getAllItems(){

        StepVerifier.create(itemReactiveRepository.findAll()) // 4
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemByID(){

        StepVerifier.create(itemReactiveRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches((item -> item.getDescription().equals("Bose Headphones")))
                .verifyComplete();


    }

    @Test
    public void findItemByDescrition() {

        StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones").log("findItemByDescrition : "))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem(){

        Item item = new Item(null,"Google Home Mini",30.00);
        Mono<Item> savedItem = itemReactiveRepository.save(item);
        StepVerifier.create(savedItem.log("saveItem : "))
                .expectSubscription()
                .expectNextMatches(item1 -> (item1.getId()!=null && item1.getDescription().equals("Google Home Mini")))
                .verifyComplete();

    }

    @Test
    public void updateItem(){

        double newPrice = 520.00;
        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice); //setting the new price
                    return item;
                })
                .flatMap((item) -> {
                    return itemReactiveRepository.save(item); //saving the item with the new price
                });

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice()==520.00)
                .verifyComplete();


    }
}