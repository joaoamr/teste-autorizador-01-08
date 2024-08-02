package com.auth.caju.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auth.caju.model.Account;
import com.auth.caju.model.Category;
import com.auth.caju.model.Merchant;
import com.auth.caju.repository.AccountRepository;
import com.auth.caju.repository.CategoryRepository;
import com.auth.caju.repository.MerchantRepository;

import jakarta.annotation.PostConstruct;

@Service
@PropertySource("classpath:transactionService.properties")
public class TransactionAuthorizationService {

	private static Logger logger = LoggerFactory.getLogger(TransactionAuthorizationService.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private Environment env;

	private Map<Long, Category> categories;
	private Map<String, Merchant> merchants;

	public String processTransaction(Long accountId, Double totalAmount, String mcc, String merchantName) {
		Optional<Account> dbAccount = accountRepository.findById(accountId);

		if (dbAccount.isEmpty()) {
			logger.info("Account [ID] " + accountId + " not found.");
			return TransactionResponseCode.DENIED;
		}

		Account account = dbAccount.get();
		Category category = null;
		
		if(merchantName != null) {
			category = getCategoryFromMerchantName(account, merchantName, mcc);
		} else {
			category = getCategoryFromMcc(account, mcc);
		}

		return process(category, account, totalAmount);
	}
	
	private Category getCategoryFromMerchantName(Account account, String name, String mcc) {
		if(merchants.containsKey(name)) {
			return merchants.get(name).getCategory();
		} else {
			//If merchant is not found, try to find it per MCC
			return getCategoryFromMcc(account, mcc);
		}
	}
	
	private Category getCategoryFromMcc(Account account, String mcc) {
		String categoryProp = env.getProperty(mcc);
		Category category = null;
		if (categoryProp != null) {
			Long categoryId = Long.parseLong(categoryProp.trim());
			category = categories.get(categoryId);
		}
		
		return category;
	}
	
	private String process(Category category, Account account, Double amount) {
		String responseCode = null;
		Double availableCash;
		if (category != null && account.getBalanceMap() != null
				&& account.getBalanceMap().containsKey(category.getCategoryId())) {

			logger.info(" Transaction [CARTEGORY] " + category.getCategoryName() + " request received.");
			// Per category transactions
			availableCash = account.getBalanceMap().get(category.getCategoryId()).getCashQt();

			if (availableCash != null && availableCash >= amount) {
				// Category
				account.getBalanceMap().get(category.getCategoryId()).setCashQt(availableCash - amount);
				responseCode = TransactionResponseCode.SUCCESS;
			} else {
				// Fallback
				if (account.getCashQt() >= amount) {
					account.setCashQt(account.getCashQt() - amount);
					responseCode = TransactionResponseCode.SUCCESS;
				} else {
					responseCode = TransactionResponseCode.DENIED;
				}
			}

		} else {
			logger.info(" Transaction [CARTEGORY] CASH request received.");
			if (account.getCashQt() >= amount) {
				account.setCashQt(account.getCashQt() - amount);
				responseCode = TransactionResponseCode.SUCCESS;
			} else {
				responseCode = TransactionResponseCode.DENIED;
			}
		}

		if (responseCode.equals(TransactionResponseCode.SUCCESS)) {
			accountRepository.save(account);
		}

		return responseCode;
	}

	@PostConstruct
	public void postConstruct() {
		categories = new HashMap<>();
		List<Category> categoriesFromDb = categoryRepository.findAll();

		for (Category cat : categoriesFromDb) {
			categories.put(cat.getCategoryId(), cat);
		}
		
		merchants = new HashMap<>();
		List<Merchant> mechantsFromDb = merchantRepository.findAll();
		
		for (Merchant merc : mechantsFromDb) {
			merchants.put(merc.getMerchantName(), merc);
		}
	}

}
