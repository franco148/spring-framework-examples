package com.fral.spring.billing.controllers;

import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

import com.fral.spring.billing.models.entity.Client;
import com.fral.spring.billing.services.ClientService;
import com.fral.spring.billing.services.UploadFileService;
import com.fral.spring.billing.utils.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> seePhoto(@PathVariable String fileName) {
		
		Resource resource = null;
		
		try {
			resource = uploadFileService.load(fileName);					
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
							 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
							 .body(resource);
	}
	
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Client client = clientService.findOne(id);
		if (client == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("client", client);
		model.put("title", "Client detail: " + client.getName());
		return "ver";
	}

	//@RequestMapping(value = "/listar", method = RequestMethod.GET)
	@GetMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = new PageRequest(page, 4);

		Page<Client> clientes = clientService.findAll(pageRequest);

		PageRender<Client> pageRender = new PageRender<Client>("/listar", clientes);
		model.addAttribute("title", "Clients list");
		model.addAttribute("clients", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	//@RequestMapping(value = "/form")
	@GetMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Client cliente = new Client();
		model.put("cliente", cliente);
		model.put("titulo", "Crear Cliente");
		return "form";
	}

	//@RequestMapping(value = "/form/{id}")
	@GetMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Client cliente = null;

		if (id > 0) {
			cliente = clientService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}

	//@RequestMapping(value = "/form", method = RequestMethod.POST)
//	@PostMapping(value = "/form")
//	public String guardar(@Valid Client cliente, BindingResult result, Model model,
//			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
//
//		if (result.hasErrors()) {
//			model.addAttribute("titulo", "Formulario de Cliente");
//			return "form";
//		}
//
//		if (!foto.isEmpty()) {
//
//			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
//					&& cliente.getFoto().length() > 0) {
//
//				uploadFileService.delete(cliente.getFoto());
//			}
//
//			String uniqueFilename = null;
//			try {
//				uniqueFilename = uploadFileService.copy(foto);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
//
//			cliente.setFoto(uniqueFilename);
//
//		}
//
//		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";
//
//		clienteService.save(cliente);
//		status.setComplete();
//		flash.addFlashAttribute("success", mensajeFlash);
//		return "redirect:listar";
//	}

//	@RequestMapping(value = "/eliminar/{id}")
//	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
//
//		if (id > 0) {
//			Cliente cliente = clienteService.findOne(id);
//
//			clienteService.delete(id);
//			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
//
//			if (uploadFileService.delete(cliente.getFoto())) {
//				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito!");
//			}
//
//		}
//		return "redirect:/listar";
//	}
}
