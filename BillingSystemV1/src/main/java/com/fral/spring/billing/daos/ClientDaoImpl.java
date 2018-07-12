package com.fral.spring.billing.daos;

//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import org.springframework.stereotype.Repository;
//
//import com.fral.spring.billing.models.Client;

//@Repository
//public class ClientDaoImpl implements ClientDao {
//	
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Client> findAll() {
//		// TODO Auto-generated method stub
//		return entityManager.createQuery("from Client").getResultList();
//	}
//
//	@Override
//	public Client findOne(Long id) {
//		return entityManager.find(Client.class, id);
//	}
//	
//	@Override
//	public void save(Client cliente) {
//		if(cliente.getId() != null && cliente.getId() > 0) {
//			entityManager.merge(cliente);
//		} else {
//			entityManager.persist(cliente);
//		}
//	}
//
//
//	@Override
//	public void delete(Long id) {
//		entityManager.remove(findOne(id));
//	}
//}
