package com.fral.spring.billing.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fral.spring.billing.daos.ClientDao;
import com.fral.spring.billing.models.Client;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientDao clientDao;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		// TODO Auto-generated method stub
		return clientDao.findAll();
	}

}
