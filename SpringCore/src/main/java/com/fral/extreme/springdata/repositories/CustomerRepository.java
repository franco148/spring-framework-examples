package com.fral.extreme.springdata.repositories;

import com.fral.extreme.springdata.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
