package com.nelioalves.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Estado;
import com.nelioalves.cursomc.dto.CidadeDto;
import com.nelioalves.cursomc.dto.EstadoDto;
import com.nelioalves.cursomc.services.CidadeService;
import com.nelioalves.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDto>> findAll() {
		
		List<Estado> estados = estadoService.findAll();
		
		List<EstadoDto> listEstadosDto = estados.stream().map(estado -> new EstadoDto(estado))
										 .collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listEstadosDto);
		
	}
	
	@RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDto>> findCidades(@PathVariable Integer estadoId){
		
		List<Cidade> cidades = cidadeService.findCidades(estadoId);
		
		List<CidadeDto> listaCidadeDto = cidades.stream().map(cidade -> new CidadeDto(cidade))
										 .collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaCidadeDto);
	}
}
