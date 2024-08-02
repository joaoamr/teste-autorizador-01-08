package com.auth.caju.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AccountCash {
	@Id @GeneratedValue Long accountCashId;
	
	@ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
	private Account account;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "categoryId")
	private Category category;
	
	private Double cashQt;
	
	public Double getCashQt() {
		return cashQt;
	}
	public void setCashQt(Double cashQt) {
		this.cashQt = cashQt;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Long getAccountCashId() {
		return accountCashId;
	}
	public void setAccountCashId(Long accountCashId) {
		this.accountCashId = accountCashId;
	}
}
