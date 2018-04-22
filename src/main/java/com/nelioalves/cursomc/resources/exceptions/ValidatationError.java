package com.nelioalves.cursomc.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidatationError extends StandartError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> listaErros = new ArrayList<>();
	
	public ValidatationError(Integer status, String mensagem, Long timeStamp) {
		super(status, mensagem, timeStamp);
		
	}

	public List<FieldMessage> getListaErros() {
		return listaErros;
	}

	public void addError(String fieldName, String mensagem) {
		listaErros.add(new FieldMessage(fieldName, mensagem));
	}

	
	
	
	
}
