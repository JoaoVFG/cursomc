package com.nelioalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.repositories.ProdutoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	
	public Produto find(Integer id) {
			
		return produtoRepository.findById(id).orElseThrow(() ->new ObjectNotFoundException(
																	"Objeto não encontrado id:" + id +
																	". Tipo: " + Pedido.class.getName()));
		
	}
	
	
	public Page<Produto> search(String nome, List<Integer> ListaIds,Integer page, Integer linesPerPage, String direction, String orderBy){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Categoria> listaCategorias = categoriaRepository.findAllById(ListaIds);
		
		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, listaCategorias, pageRequest);
	}
}
