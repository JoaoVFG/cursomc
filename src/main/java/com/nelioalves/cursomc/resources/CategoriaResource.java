package com.nelioalves.cursomc.resources;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDto;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	CategoriaService categoriaService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {

		Categoria categoria = categoriaService.find(id);

		return ResponseEntity.ok().body(categoria);

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDto>> findAll() {

		List<Categoria> listaCategorias = categoriaService.findAll();

		List<CategoriaDto> listaCategoriasDTO = listaCategorias.stream().map(categoria -> new CategoriaDto(categoria))
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(listaCategoriasDTO);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDto categoriaDto) {
		
		Categoria categoria = categoriaService.fromDto(categoriaDto);
		
		categoria = categoriaService.insert(categoria);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId())
				.toUri();

		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDto categoriaDto, @PathVariable Integer id) {
		
		Categoria categoria = categoriaService.fromDto(categoriaDto);
		
		categoria.setId(id);

		categoria = categoriaService.update(categoria);

		return ResponseEntity.noContent().build();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		categoriaService.delete(id);

		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDto>> findPage(
												@RequestParam(name = "page", defaultValue = "0") Integer page,
												@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
												@RequestParam(name = "direction", defaultValue = "ASC") String direction,
												@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy) {
			
		Page<Categoria> listaCategorias = categoriaService.findPage(page, linesPerPage, direction, orderBy);

		Page<CategoriaDto> listaCategoriasDTO = listaCategorias.map(categoria -> new CategoriaDto(categoria));
		
		return ResponseEntity.ok().body(listaCategoriasDTO);
		
	}

}
