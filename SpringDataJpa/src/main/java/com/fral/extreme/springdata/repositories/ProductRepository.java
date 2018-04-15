package com.fral.extreme.springdata.repositories;

import com.fral.extreme.springdata.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
