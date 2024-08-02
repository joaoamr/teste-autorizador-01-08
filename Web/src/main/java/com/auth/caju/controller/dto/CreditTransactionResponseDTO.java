package com.auth.caju.controller.dto;

public class CreditTransactionResponseDTO {
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public CreditTransactionResponseDTO() {
		super();
	}

	public CreditTransactionResponseDTO(String code) {
		super();
		this.code = code;
	}
	
	
}
