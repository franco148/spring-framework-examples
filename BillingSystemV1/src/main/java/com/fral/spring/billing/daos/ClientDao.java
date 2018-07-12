package com.fral.spring.billing.daos;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.fral.spring.billing.models.Client;

public interface ClientDao extends PagingAndSortingRepository<Client, Long> {
		
}
