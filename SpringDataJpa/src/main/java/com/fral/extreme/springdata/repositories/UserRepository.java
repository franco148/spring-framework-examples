package com.fral.extreme.springdata.repositories;

import com.fral.extreme.springdata.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
