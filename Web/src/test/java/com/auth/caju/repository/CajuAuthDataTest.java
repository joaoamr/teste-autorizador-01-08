package com.auth.caju.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import com.auth.caju.model.Account;
import com.auth.caju.model.AccountCash;
import com.auth.caju.model.Category;
import com.auth.caju.model.Merchant;


@DataJpaTest(properties = {
	    "spring.datasource.url=jdbc:h2:mem:testdb",
	    "spring.jpa.hibernate.ddl-auto=create-drop"
	})
@TestInstance(Lifecycle.PER_CLASS)
public class CajuAuthDataTest {
	
	@Autowired
	private AccountCashRepository accountCashRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private MerchantRepository merchantRepository;
	
	@BeforeAll
	private void insertData() {
		
		Category mealCategory = new Category();
		mealCategory.setCategoryId(0L);
		mealCategory.setCategoryName("MEAL");
		categoryRepository.save(mealCategory);
		
		Category foodCategory = new Category();
		foodCategory.setCategoryId(1L);
		foodCategory.setCategoryName("FOOD");
		categoryRepository.save(foodCategory);
		
		Account account1 = new Account();
		account1.setHolderName("Joao Rodrigues");
		account1.setCashQt(100.0);
		accountRepository.save(account1);
		Account account2 = new Account();
		account2.setHolderName("Antonio Magri");
		account2.setCashQt(200.0);
		accountRepository.save(account2);
		
		AccountCash mccCash1 = new AccountCash();
		mccCash1.setCategory(foodCategory);
		mccCash1.setCashQt(120.00);
		mccCash1.setAccount(account1);
		
		AccountCash mccCash2 = new AccountCash();
		mccCash2.setCategory(mealCategory);
		mccCash2.setCashQt(220.00);
		mccCash2.setAccount(account2);
		
		AccountCash mccCash3 = new AccountCash();
		mccCash3.setCategory(foodCategory);
		mccCash3.setCashQt(320.50);
		mccCash3.setAccount(account2);
		
		accountCashRepository.save(mccCash1);
		accountCashRepository.save(mccCash2);
		accountCashRepository.save(mccCash3);
		
		Merchant mealMerchant = new Merchant();
		mealMerchant.setCategory(mealCategory);
		mealMerchant.setMerchantName("MEAL                        SAO PAULO BR");
		mealMerchant.setMerchantId(0L);
		
		Merchant foodMerchant = new Merchant();
		foodMerchant.setCategory(foodCategory);
		foodMerchant.setMerchantName("FOOD                        SAO PAULO BR");
		foodMerchant.setMerchantId(1L);
		
		Merchant cashMerchant = new Merchant();
		cashMerchant.setMerchantName("CASH                        SAO PAULO BR");
		cashMerchant.setMerchantId(2L);
		
		merchantRepository.save(cashMerchant);
		merchantRepository.save(foodMerchant);
		merchantRepository.save(mealMerchant);
		
	}
	
	@Test
	public void loadAccounts() {	

		List<Account> accounts = accountRepository.findAll();
		
		assertTrue(accounts.size() == 2);
		
		Set<String> names = new HashSet<>(Arrays.asList(accounts.get(0).getHolderName(),
				accounts.get(1).getHolderName()));
		
		assertTrue(names.contains("Antonio Magri"));
		assertTrue(names.contains("Joao Rodrigues"));
		
	}
	
	@Test
	public void loadAccountsMccCash() {
		
		List<AccountCash> mccCash = accountCashRepository.findAll();
		
		assertTrue(!mccCash.isEmpty());
		
		List<Account> accounts = accountRepository.findAll(Sort.by(Sort.Direction.ASC, "accountId"));
		
		assertEquals(accounts.get(0).getBalanceMap().get(1L).getCategory().getCategoryName(), "FOOD");
		assertEquals(accounts.get(0).getBalanceMap().get(1L).getCashQt(), 120);
		
		assertEquals(accounts.get(1).getBalanceMap().get(0L).getCategory().getCategoryName(), "MEAL");
		assertEquals(accounts.get(1).getBalanceMap().get(0L).getCashQt(), 220);
		
		assertEquals(accounts.get(1).getBalanceMap().get(1L).getCategory().getCategoryName(), "FOOD");
		assertEquals(accounts.get(1).getBalanceMap().get(1L).getCashQt(), 320.50);
	}
	
}
