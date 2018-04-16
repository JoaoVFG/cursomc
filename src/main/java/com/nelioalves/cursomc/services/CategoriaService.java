package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.Repositories.CategoriaRepository;
import com.nelioalves.cursomc.domain.Categoria;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria buscar (Integer id) {
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		//return categoriaRepository.findById(id).orElse(null);
		return categoria.orElse(null);
	}
	
}
