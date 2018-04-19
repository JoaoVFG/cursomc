package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.Repositories.PedidoRepository;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	public Pedido find (Integer id){
		
		Optional<Pedido> pedido =  pedidoRepository.findById(id);
		
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
											"Objeto não encontrado id:" + id +
											". Tipo: " + Pedido.class.getName()));
		
	}
	
}
