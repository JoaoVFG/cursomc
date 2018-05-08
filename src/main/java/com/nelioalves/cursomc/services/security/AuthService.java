package com.nelioalves.cursomc.services.security;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import com.nelioalves.cursomc.services.mail.EmailService;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPassword = newPassword();
		
		cliente.setSenha(bCryptPasswordEncoder.encode(newPassword));
		
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPassword);
		
	}


	private String newPassword() {
		char [] vet = new char[10];
		
		for (int i = 0; i < vet.length; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}


	private char randomChar() {
		int option = random.nextInt(3);
		
		if(option == 0 ) {//gera digito
			return (char) (random.nextInt(10) + 48);
		}else if(option == 1){//gera letra Maiuscula
			return (char) (random.nextInt(26) + 65);
		}else {//gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}
	}
	
	
	
}
