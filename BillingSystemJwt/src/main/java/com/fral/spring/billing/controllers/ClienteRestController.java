package com.fral.spring.billing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fral.spring.billing.services.ClientService;
import com.fral.spring.billing.view.xml.ClienteList;

@RestController
@RequestMapping("/api/clients")
public class ClienteRestController {

	@Autowired
	private ClientService clientServices;
	
	
	@GetMapping(value = "/listar")
	public ClienteList listar() {
		return new ClienteList(clientServices.findAll());
	}

}
