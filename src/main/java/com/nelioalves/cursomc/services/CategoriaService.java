package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.Repositories.CategoriaRepository;
import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDto;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria find(Integer id) {
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		
		return categoria.orElseThrow(() -> new ObjectNotFoundException(
											   "Objeto não encontrado! Id: " + id +
											   ". Tipo: " + Categoria.class.getName()));
	}
	
	
	public List<Categoria>findAll() {
		
		return categoriaRepository.findAll();
	
	}
	
	
	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return categoriaRepository.save(categoria);
	}
	
	
	
	public Categoria update(Categoria updateCategoria) {
		
		Categoria categoria = find(updateCategoria.getId());
		
		updateData(categoria, updateCategoria);
		
		return categoriaRepository.save(categoria);
	}
	
	
	public void delete(Integer id) {
		find(id);
		
		try {
			categoriaRepository.deleteById(id);
		}
		catch(DataIntegrityViolationException erro) {
			throw new DataIntegrityException("NAO E POSSIVEL EXCLUIR UMA CATEGORIA QUE POSSUA PRODUTOS");
		}
	}
	
	
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return categoriaRepository.findAll(pageRequest);
	}
	
	
	public Categoria fromDto(CategoriaDto categoriaDto) {
		return new Categoria(categoriaDto.getId(), categoriaDto.getNome());
	}
	
	private void updateData(Categoria categoria, Categoria updateCategoria) {
		categoria.setNome(updateCategoria.getNome());
	}
	
}
