package com.nelioalves.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.Perfil;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDto;
import com.nelioalves.cursomc.dto.ClienteNewDto;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.amazon.S3Service;
import com.nelioalves.cursomc.services.exceptions.AuthorizationException;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import com.nelioalves.cursomc.services.media.ImagemService;
import com.nelioalves.cursomc.services.security.UserService;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder cryptPasswordEncoder;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImagemService imagemService;
	
	@Value("${img.prefix.client.profile}")
	private String prefixo;
	
	@Value("${img.profile.size}")
	private int size;
	

	public Cliente find (Integer id) {
		
		UserSS userSS = UserService.authenticated();
		
		if(userSS==null || !userSS.hasRole(Perfil.ADMIN) && !id.equals(userSS.getId())) {
			
			throw new AuthorizationException("Acesso Negado");
		
		}
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		
		return cliente.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
																	   ". Tipo: " + Cliente.class.getName()));
	}
	
	
	
	public List<Cliente> findAll(){
		
		return clienteRepository.findAll();
		
	}
	
	
	public Cliente findByEmail(String email) {
		
		UserSS userSS = UserService.authenticated();
		
		if(userSS==null || !userSS.hasRole(Perfil.ADMIN) && !email.equals(userSS.getUsername())) {
			System.out.println("Acesso negado!");
			System.out.println(userSS.getUsername().toString());
			throw new AuthorizationException("Acesso Negado");
		
		}
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! ID: " + userSS.getId() + 
											  ". Tipo: " + Cliente.class.getName());
		}
		
		return cliente;
		
	}
	
	
	public Cliente insert(Cliente cliente) {
		
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		
		return cliente;
		
	}
	
	
	public Cliente update(Cliente updateCliente) {
		
		UserSS userSS = UserService.authenticated();
		
		if(userSS==null || !userSS.hasRole(Perfil.ADMIN) && !updateCliente.getId().equals(userSS.getId())) {
			
			throw new AuthorizationException("Acesso Negado");
		
		}
		
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
	
	
	public URI uploadProfilePicture(MultipartFile multipartFile){
		
		UserSS userSS = UserService.authenticated();
		
		if(userSS==null) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		BufferedImage bufferedImage = imagemService.getJpgImageFromFile(multipartFile);
		
		bufferedImage = imagemService.cropSquare(bufferedImage);
		bufferedImage = imagemService.resize(bufferedImage, size);
		
		String fileName = prefixo + userSS.getId() + ".jpg";
		
		return s3Service.uploadFile(imagemService.getInputStream(bufferedImage, "jpg"), fileName, "image");
		
		
	}
	
}
