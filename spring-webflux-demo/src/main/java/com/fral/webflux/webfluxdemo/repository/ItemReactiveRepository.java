package com.fral.webflux.webfluxdemo.repository;

import com.fral.webflux.webfluxdemo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item,String> {
}
