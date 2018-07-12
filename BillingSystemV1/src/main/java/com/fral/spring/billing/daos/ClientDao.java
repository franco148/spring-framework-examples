package com.fral.spring.billing.daos;

import java.util.List;

import com.fral.spring.billing.models.Client;

public interface ClientDao {
	
	List<Client> findAll();

	void save(Client client);
	
	Client findOne(Long id);
	
	void delete(Long id);

}
