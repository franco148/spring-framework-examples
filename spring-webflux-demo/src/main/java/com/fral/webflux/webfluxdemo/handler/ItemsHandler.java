package com.fral.webflux.webfluxdemo.handler;


import com.fral.webflux.webfluxdemo.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemsHandler {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;
}
