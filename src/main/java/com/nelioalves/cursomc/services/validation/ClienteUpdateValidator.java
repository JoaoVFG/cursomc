package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.nelioalves.cursomc.Repositories.ClienteRepository;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDto;
import com.nelioalves.cursomc.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDto> {
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDto objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		Cliente clienteAuxiliar = clienteRepository.findByEmail(objDto.getEmail());
		
		List<FieldMessage> listErros = new ArrayList<>();
		
		
		if(clienteAuxiliar != null &&
		   !clienteAuxiliar.getId().equals(Integer.parseInt(map.get("id")))) {
			
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