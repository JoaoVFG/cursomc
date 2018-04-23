package com.nelioalves.cursomc.resources;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDto;
import com.nelioalves.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	
	@Autowired
	ClienteService clienteService;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		
		Cliente cliente = clienteService.find(id);
		
		return ResponseEntity.ok().body(cliente);
		
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDto>> findAll() {
		
		List<Cliente> listacliente = clienteService.findAll();
		
		List<ClienteDto> listaclienteDto = listacliente.stream().map(cliente -> new ClienteDto(cliente))
											.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaclienteDto);
		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDto clienteDto, @PathVariable Integer id){
		Cliente cliente = clienteService.fromDto(clienteDto);
		
		cliente.setId(id);
		
		cliente = clienteService.update(cliente);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		clienteService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDto>> findPage(
												@RequestParam(name = "page", defaultValue = "0") Integer page,
												@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
												@RequestParam(name = "direction", defaultValue = "ASC") String direction,
												@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy) {
			
		Page<Cliente> listaClientes = clienteService.findPage(page, linesPerPage, direction, orderBy);
		
		Page<ClienteDto> listaClientesDTO = listaClientes.map(cliente -> new ClienteDto(cliente));
		
		return ResponseEntity.ok().body(listaClientesDTO);
	}
	
	
}
