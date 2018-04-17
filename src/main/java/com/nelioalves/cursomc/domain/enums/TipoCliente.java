package com.nelioalves.cursomc.domain.enums;

public enum TipoCliente {
	PESSOAFISICA(1, "Pessoa Fisica"), 
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private Integer codigo;
	private String descricao;
	
	private TipoCliente(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoCliente toEnum(Integer cod) {
		
		if(cod == null) {
			return null;
		}
		
		for(TipoCliente tipoCliente : TipoCliente.values()) {
			if(cod.equals(tipoCliente.getCodigo())) {
				return tipoCliente;
			}
		}
		
		
		throw new IllegalArgumentException("id Inválido: " + cod);
		
	}
	
}
