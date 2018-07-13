package com.fral.spring.billing.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fral.spring.billing.models.Client;
import com.fral.spring.billing.services.ClientService;
import com.fral.spring.billing.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final static String UPLOADS_FOLDER = "uploads";
	
	
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> seePhoto(@PathVariable String filename) {
		Path pathPhoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
		log.info("pathFoto: " + pathPhoto);
		Resource recurso = null;
		try {
			recurso = new UrlResource(pathPhoto.toUri());
			if(!recurso.exists() || !recurso.isReadable()) {
				throw new RuntimeException("Error: Image can not be loaded: " + pathPhoto.toString());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +  recurso.getFilename() +"\"")
				.body(recurso);
	}
	
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Client cliente = clientService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "Client does not exist in the system.");
			return "redirect:/listar";
		}

		model.put("client", cliente);
		model.put("title", "Client detail: " + cliente.getName());
		return "ver";
	}
	
	@GetMapping("/listar")
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = new PageRequest(page, 4);
		
		Page<Client> clientes = clientService.findAll(pageRequest);
		
		PageRender<Client> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("title", "Clients List");
		model.addAttribute("clients", clientes);
		model.addAttribute("page", pageRender);
		
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
	public String guardar(@Valid Client cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile photo,
						  RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			return "form";
		}
		
		if (!photo.isEmpty()) {
			
			if(cliente.getId() !=null 
					&& cliente.getId() > 0
					&& cliente.getPhoto()!=null
					&& cliente.getPhoto().length() > 0) {
				
				Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getPhoto()).toAbsolutePath();
				File archivo = rootPath.toFile();
				
				if(archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}
			}
						
			// SECOND VERSION: Absolute and external directory
			String uniqueFilename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFilename);

			Path rootAbsolutPath = rootPath.toAbsolutePath();
			
			log.info("rootPath: " + rootPath);
			log.info("rootAbsolutPath: " + rootAbsolutPath);
			
			try {

				// FIRST VERSION: Uploads to external directory
//				String rootPath = "D://CODE//TMP//SpringFrameworkCourse//uploads";
//				byte[] bytes = photo.getBytes();
//				Path rutaCompleta = Paths.get(rootPath + "//" + photo.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);
//				flash.addFlashAttribute("info", "Has subido correctamente '" + photo.getOriginalFilename() + "'");
//
//				cliente.setPhoto(photo.getOriginalFilename());
				
				// SECOND VERSION: Absolute and external directory
				Files.copy(photo.getInputStream(), rootAbsolutPath);
				
				flash.addFlashAttribute("info", "Image loaded successfully '" + uniqueFilename + "'");

				cliente.setPhoto(uniqueFilename);

			} catch (IOException e) {
				e.printStackTrace();
			}

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
			
			Client cliente = clientService.findOne(id);
			
			clientService.delete(id);
			flash.addFlashAttribute("success", "Client deleted successfully!");
			
			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getPhoto()).toAbsolutePath();
			File archivo = rootPath.toFile();
			
			if(archivo.exists() && archivo.canRead()) {
				if(archivo.delete()) {
					flash.addFlashAttribute("info", "Photo " + cliente.getPhoto() + " removed successfully!");
				}
			}

		}
		return "redirect:/listar";
	}
}
