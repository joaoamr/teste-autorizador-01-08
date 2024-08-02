package com.auth.caju.controller.dto;

public class CreditTransactionRequestDTO {
	private String account;
	private Double totalAmount;
	private String mcc;
	private String merchant;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	public CreditTransactionRequestDTO() {
		super();
	}
	
	public CreditTransactionRequestDTO(String account, Double totalAmount, String mcc, String merchant) {
		super();
		this.account = account;
		this.totalAmount = totalAmount;
		this.mcc = mcc;
		this.merchant = merchant;
	}
	
	
	
}
