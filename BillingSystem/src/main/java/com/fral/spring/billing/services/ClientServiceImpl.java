package com.fral.spring.billing.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fral.spring.billing.models.dao.ClientDao;
import com.fral.spring.billing.models.entity.Client;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientDao clientDao;

	
	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {

		return (List<Client>)clientDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable pageable) {
		
		return clientDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Client client) {
		
		clientDao.save(client);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findOne(Long id) {
		
		Optional<Client> clientFromDb = clientDao.findById(id); 
		return clientFromDb.isPresent() ? clientFromDb.get() : null;
	}

	@Override
	public void delete(Long id) {
		
		clientDao.deleteById(id);
	}

}
