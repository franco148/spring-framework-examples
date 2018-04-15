package com.fral.extreme.springdata.repositories;

import com.fral.extreme.springdata.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
