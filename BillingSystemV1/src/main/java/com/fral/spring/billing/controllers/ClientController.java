package com.fral.spring.billing.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fral.spring.billing.models.Client;
import com.fral.spring.billing.services.ClientService;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("title", "Clients List");
		model.addAttribute("clients", clientService.findAll());
		return "listar";
	}

	//@RequestMapping(value = "/form")
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {

		Client cliente = new Client();
		model.put("client", cliente);
		model.put("title", "Client Form");
		return "form";
	}
	
	//@RequestMapping(value="/form/{id}")
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		
		Client cliente = null;
		
		if(id > 0) {
			cliente = clientService.findOne(id);
		} else {
			return "redirect:/listar";
		}
		model.put("client", cliente);
		model.put("title", "Edit a Cliente");
		return "form";
	}

	//@RequestMapping(value = "/form", method = RequestMethod.POST)
	@PostMapping("/form")
	public String guardar(@Valid Client cliente, BindingResult result, Model model, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			return "form";
		}

		clientService.save(cliente);
		status.setComplete();
		return "redirect:listar";
	}
	
	//@RequestMapping(value="/eliminar/{id}")
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Long id) {
		
		if(id > 0) {
			clientService.delete(id);
		}
		return "redirect:/listar";
	}
}
