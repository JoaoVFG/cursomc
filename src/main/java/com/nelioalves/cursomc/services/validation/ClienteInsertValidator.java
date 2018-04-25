package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.nelioalves.cursomc.Repositories.ClienteRepository;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteNewDto;
import com.nelioalves.cursomc.resources.exceptions.FieldMessage;
import com.nelioalves.cursomc.services.validation.utils.Br;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDto> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDto objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> listErros = new ArrayList<>();
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && 
			!Br.isValidCPF(objDto.getCpfOuCnpj())){
				listErros.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && 
				!Br.isValidCNPJ(objDto.getCpfOuCnpj())){
					listErros.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
			}
		
		
		if(clienteRepository.findByEmail(objDto.getEmail()) != null) {
			listErros.add(new FieldMessage("Email", "Email ja existente"));
		}
		
		
		for (FieldMessage erro : listErros) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(erro.getMessage()).addPropertyNode(erro.getFieldName())
					.addConstraintViolation();
		}
		
		return listErros.isEmpty();
	}
}