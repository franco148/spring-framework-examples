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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@GetMapping("/form")
	public String crear(Map<String, Object> model) {

		Client cliente = new Client();
		model.put("client", cliente);
		model.put("title", "Create Client");
		return "form";
	}
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Client cliente = null;
		
		if(id > 0) {
			cliente = clientService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "The client ID does not exist in the system!");
				return "redirect:/listar";
			}

		} else {
			flash.addFlashAttribute("error", "The client ID can not be zero!");
			return "redirect:/listar";
		}
		model.put("client", cliente);
		model.put("title", "Edit a Cliente");
		return "form";
	}

	@PostMapping("/form")
	public String guardar(@Valid Client cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			return "form";
		}
		
		String flashMessage = (cliente.getId() != null)? "Client has been edited successfully!" : "Client has been created successfully!";

		clientService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);

		return "redirect:listar";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash) {
		
		if(id > 0) {
			clientService.delete(id);
			flash.addFlashAttribute("success", "Client deleted successfully!");
		}
		return "redirect:/listar";
	}
}
