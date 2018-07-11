package com.fral.spring.billing.daos;

import java.util.List;

import com.fral.spring.billing.models.Client;

public interface ClientDao {
	
	public List<Client> findAll();

}
