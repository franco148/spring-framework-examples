package com.fral.spring.di.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fral.spring.di.services.MyService;

@Controller
public class IndexController {

	// The following line is not taking into account DI.
//	private MyService myService = new MyService();
	
	// The following line is taking into account DI
	// The injection of the service can also be done through setters and constructors.
	@Autowired
	private MyService myService;
	
	@GetMapping({"/", "", "index"})
	public String index(Model model) {
		model.addAttribute("object", myService.operation());
		return "index";
	}
}
