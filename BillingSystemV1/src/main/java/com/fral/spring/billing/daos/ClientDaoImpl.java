package com.fral.spring.billing.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.fral.spring.billing.models.Client;

@Repository
public class ClientDaoImpl implements ClientDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> findAll() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from Client").getResultList();
	}

}
