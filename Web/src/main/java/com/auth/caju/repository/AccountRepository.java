package com.auth.caju.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.auth.caju.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
