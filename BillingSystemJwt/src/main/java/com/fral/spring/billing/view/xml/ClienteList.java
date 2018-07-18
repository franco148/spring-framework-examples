package com.fral.spring.billing.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fral.spring.billing.models.entity.Client;

@XmlRootElement(name="clients")
public class ClienteList {

	@XmlElement(name="cliente")
	public List<Client> clientes;

	public ClienteList() {}


	public ClienteList(List<Client> clientes) {
		this.clientes = clientes;
	}

	public List<Client> getClientes() {
		return clientes;
	}

}
