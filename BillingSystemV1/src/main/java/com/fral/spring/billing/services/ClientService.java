package com.fral.spring.billing.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fral.spring.billing.models.Client;

public interface ClientService {

	List<Client> findAll();
	
	Page<Client> findAll(Pageable pageable);

	void save(Client client);
	
	Client findOne(Long id);
	
	void delete(Long id);


}
