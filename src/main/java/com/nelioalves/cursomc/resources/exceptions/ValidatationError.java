package com.nelioalves.cursomc.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidatationError extends StandartError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> listaErros = new ArrayList<>();
	
	

	public ValidatationError(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getListaErros() {
		return listaErros;
	}

	public void addError(String fieldName, String mensagem) {
		listaErros.add(new FieldMessage(fieldName, mensagem));
	}

	
	
	
	
}
