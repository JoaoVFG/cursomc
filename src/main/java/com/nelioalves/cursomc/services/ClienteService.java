package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDto;
import com.nelioalves.cursomc.dto.ClienteNewDto;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder cryptPasswordEncoder;

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
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		
		return cliente;
		
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
			throw new DataIntegrityException("NAO E POSSIVEL EXCLUIR UM CLIENTE QUE POSSUE PEDIDOS CADASTRADOS");
		}
		
	}
	
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pageRequest);
	}
	
	
	public Cliente fromDto(ClienteDto clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDto(ClienteNewDto clienteNewDto) {
		Cliente cliente = new Cliente(null, 
									  clienteNewDto.getNome(), 
									  clienteNewDto.getEmail(), 
									  clienteNewDto.getCpfOuCnpj(), 
									  TipoCliente.toEnum(clienteNewDto.getTipo()),
									  cryptPasswordEncoder.encode(clienteNewDto.getSenha())); 
		
		Cidade cidade = new Cidade(clienteNewDto.getCidadeId(), null, null);
		
		Endereco endereco = new Endereco(null,
										 clienteNewDto.getLogradouro(), 
										 clienteNewDto.getNumero(), 
										 clienteNewDto.getComplemento(), 
										 clienteNewDto.getBairro(), 
										 clienteNewDto.getCep(), 
										 cliente, 
										 cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNewDto.getTelefone1());
		
		if(clienteNewDto.getTelefone2()!= null) cliente.getTelefones().add(clienteNewDto.getTelefone2());
		if(clienteNewDto.getTelefone3()!= null) cliente.getTelefones().add(clienteNewDto.getTelefone3());
		
		return cliente;
		
	}
	
	
	
	private void updateData(Cliente cliente, Cliente updateCliente) {
		cliente.setNome(updateCliente.getNome());
		cliente.setEmail(updateCliente.getEmail());
	}
	
}
