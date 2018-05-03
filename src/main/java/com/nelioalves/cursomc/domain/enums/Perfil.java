package com.nelioalves.cursomc.domain.enums;

public enum Perfil {

	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String role;
	
	
	public int getCod() {
		return codigo;
	}
	
	public String getRole() {
		return role;
	}

	private Perfil(int codigo, String role) {
		this.codigo = codigo;
		this.role = role;
	}
	
	public static Perfil toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (Perfil perfil: Perfil.values()) {
			if (codigo.equals(perfil.getCod())) {
				return perfil;
			}
		}
		
		throw new IllegalArgumentException("Código inválido: " + codigo);
	}
	
}
