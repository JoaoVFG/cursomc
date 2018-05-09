package com.nelioalves.cursomc.resources;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDto;
import com.nelioalves.cursomc.dto.ClienteNewDto;
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
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDto>> findAll() {
		
		List<Cliente> listacliente = clienteService.findAll();
		
		List<ClienteDto> listaclienteDto = listacliente.stream().map(cliente -> new ClienteDto(cliente))
											.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaclienteDto);
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDto clienteNewDto){
		
		Cliente cliente = clienteService.fromDto(clienteNewDto);
		
		cliente = clienteService.insert(cliente);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
											 .path("/{id}")
											 .buildAndExpand(cliente.getId())
											 .toUri();
		
		return ResponseEntity.created(uri).build();
		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDto clienteDto, @PathVariable Integer id){
		Cliente cliente = clienteService.fromDto(clienteDto);
		
		cliente.setId(id);
		
		cliente = clienteService.update(cliente);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		clienteService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
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
	
	@RequestMapping(value = "/picture")
	public ResponseEntity uploadProfilePicture(@RequestParam(name = "file") MultipartFile multipartFile){
		
		URI uri = clienteService.uploadProfilePicture(multipartFile);
		
		return ResponseEntity.created(uri).build();
	}
	
}
