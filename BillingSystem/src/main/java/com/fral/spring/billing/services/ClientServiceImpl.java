package com.fral.spring.billing.services;

import static org.assertj.core.api.Assertions.in;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fral.spring.billing.models.dao.ClientDao;
import com.fral.spring.billing.models.dao.InvoiceDao;
import com.fral.spring.billing.models.dao.ProductDao;
import com.fral.spring.billing.models.entity.Client;
import com.fral.spring.billing.models.entity.Invoice;
import com.fral.spring.billing.models.entity.Product;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private InvoiceDao invoiceDao;

	
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

	@Override
	public List<Product> findProductByName(String term) {
		// TODO Auto-generated method stub
		return productDao.findByNameLikeIgnoreCase("%"+term+"%");
	}

	@Override
	public void saveInvoice(Invoice invoice) {
		// TODO Auto-generated method stub
		invoiceDao.save(invoice);
	}

	@Override
	public Product findProductoById(Long id) {
		// TODO Auto-generated method stub
		Optional<Product> productFromDb = productDao.findById(id);
		return  productFromDb.isPresent() ? productFromDb.get() : null;
	}

	@Override
	public Invoice findInvoiceById(Long id) {
		// TODO Auto-generated method stub
		Optional<Invoice> invoiceFromDb = invoiceDao.findById(id);
		return invoiceFromDb.isPresent() ? invoiceFromDb.get() : null;
	}

	@Override
	public void deleteInvoice(Long id) {
		// TODO Auto-generated method stub
		invoiceDao.deleteById(id);
	}

}
