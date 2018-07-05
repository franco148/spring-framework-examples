package com.fral.spring.billing.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.fral.spring.billing.models.entity.Client;

public interface ClientDao extends PagingAndSortingRepository<Client, Long> {

}
