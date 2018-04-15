package com.fral.extreme.springdata.repositories;

import com.fral.extreme.springdata.domain.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository  extends CrudRepository<Role, Integer> {
}
