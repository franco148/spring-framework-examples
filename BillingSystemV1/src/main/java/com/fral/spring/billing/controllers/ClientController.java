package com.fral.spring.billing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fral.spring.billing.services.ClientService;

@Controller
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("title", "Clients List");
		model.addAttribute("clients", clientService.findAll());
		return "listar";
	}

}
