package com.nelioalves.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.PedidoService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	PedidoService pedidoService;
	
	@ApiOperation(value="Busca Pedido Por Id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id){
		Pedido pedido = pedidoService.find(id);
		return ResponseEntity.ok(pedido);
	}
	
	@ApiOperation(value="Cria um  Pedido")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Pedido pedido){
		pedido = pedidoService.insert(pedido);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				 .path("/{id}")
				 .buildAndExpand(pedido.getId())
				 .toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value="Busca de Pedidos paginados")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
												@RequestParam(name = "page", defaultValue = "0") Integer page,
												@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
												@RequestParam(name = "direction", defaultValue = "DESC") String direction,
												@RequestParam(name = "orderBy", defaultValue = "instante") String orderBy) {
		
		Page<Pedido> listaPedidos = pedidoService.findPage(page, linesPerPage, direction, orderBy);
		
		return ResponseEntity.ok().body(listaPedidos);
		
	}
}
