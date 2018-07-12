package com.fral.spring.billing.services;

import java.util.List;

import com.fral.spring.billing.models.Client;

public interface ClientService {

	List<Client> findAll();

}
