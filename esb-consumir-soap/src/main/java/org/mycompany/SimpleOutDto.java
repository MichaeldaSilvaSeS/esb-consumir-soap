package org.mycompany;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleOutDto {

	@JsonProperty("mensagem")
	private String mensagem;
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
