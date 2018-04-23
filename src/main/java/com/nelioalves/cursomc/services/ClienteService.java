package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.Repositories.ClienteRepository;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDto;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente find (Integer id) {
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		
		return cliente.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
																	   ". Tipo: " + Cliente.class.getName()));
	}
	
	
	
	public List<Cliente> findAll(){
		
		return clienteRepository.findAll();
		
	}
	
	
	public Cliente insert(Cliente cliente) {
		
		cliente.setId(null);
		
		return clienteRepository.save(cliente);
		
	}
	
	
	public Cliente update(Cliente updateCliente) {
		
		Cliente cliente = find(updateCliente.getId());
		
		updateData(cliente, updateCliente);
		
		return clienteRepository.save(cliente);
	}
	
	
	public void delete(Integer id) {
		find(id);
		
		try {
			clienteRepository.deleteById(id);
		}
		catch (DataIntegrityViolationException erro) {
			throw new DataIntegrityException("NAO E POSSIVEL EXCLUIR UM CLIENTE QUE POSSUE PEDIDOS E TELEFONES CADASTRADOS");
		}
		
	}
	
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pageRequest);
	}
	
	
	public Cliente fromDto(ClienteDto clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null);
	}
	
	
	
	private void updateData(Cliente cliente, Cliente updateCliente) {
		cliente.setNome(updateCliente.getNome());
		cliente.setEmail(updateCliente.getEmail());
	}
	
}
