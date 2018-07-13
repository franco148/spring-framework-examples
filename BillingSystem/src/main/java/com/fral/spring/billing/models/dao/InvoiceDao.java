package com.fral.spring.billing.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.fral.spring.billing.models.entity.Invoice;

public interface InvoiceDao extends CrudRepository<Invoice, Long> {

}
