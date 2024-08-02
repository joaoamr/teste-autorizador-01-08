package com.auth.caju.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.auth.caju.model.AccountCash;

public interface AccountCashRepository extends JpaRepository<AccountCash, Long>{
	
}
