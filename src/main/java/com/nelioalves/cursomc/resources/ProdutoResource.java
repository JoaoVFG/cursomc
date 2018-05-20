package com.nelioalves.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.dto.ProdutoDto;
import com.nelioalves.cursomc.resources.utils.Uri;
import com.nelioalves.cursomc.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoResource {
	
	@Autowired
	ProdutoService produtoService;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id){
		
		Produto produto = produtoService.find(id);
		
		return ResponseEntity.ok(produto);
		
	}

	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDto>> findPage(
												@RequestParam(name = "nome", defaultValue = "")String nome,
												@RequestParam(name = "categorias", defaultValue = "")String categorias,
												
												@RequestParam(name = "page", defaultValue = "0") Integer page,
												@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
												@RequestParam(name = "direction", defaultValue = "ASC") String direction,
												@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy) {
		
		
		String nomeDecoded = Uri.decodeParam(nome);
		List<Integer> listaIds = Uri.decodeIntList(categorias);
		
		Page<Produto> listaProdutos = produtoService.search(nomeDecoded, listaIds, page, linesPerPage, direction, orderBy);

		Page<ProdutoDto> listaProdutosDto = listaProdutos.map(Produto -> new ProdutoDto(Produto));
		
		return ResponseEntity.ok().body(listaProdutosDto);
	}
	
	
}
