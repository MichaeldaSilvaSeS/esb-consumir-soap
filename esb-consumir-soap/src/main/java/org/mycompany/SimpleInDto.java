package org.mycompany;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleInDto {

	@JsonProperty("nome")
	private String nome;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
}
