package com.auth.caju.model;

import java.util.Map;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Account {
	@Id @GeneratedValue private Long accountId;
	private String holderName;
	
	@OneToMany(mappedBy = "account")
	@MapKeyColumn(name = "categoryId")
	Map<Long, AccountCash> balanceMap;
	
	private Double cashQt;
	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public Map<Long, AccountCash> getBalanceMap() {
		return balanceMap;
	}

	public void setBalanceMap(Map<Long, AccountCash> balanceMap) {
		this.balanceMap = balanceMap;
	}

	public Double getCashQt() {
		return cashQt;
	}

	public void setCashQt(Double cashQt) {
		this.cashQt = cashQt;
	}
}
