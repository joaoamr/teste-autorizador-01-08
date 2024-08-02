package com.auth.caju.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth.caju.controller.dto.CreditTransactionRequestDTO;
import com.auth.caju.controller.dto.CreditTransactionResponseDTO;
import com.auth.caju.service.TransactionAuthorizationService;
import com.auth.caju.service.TransactionResponseCode;

@RestController
public class CreditController {
	
	private static Logger logger = LoggerFactory.getLogger(CreditController.class);
	
	@Autowired
	TransactionAuthorizationService transactionAuthorizationService;
	
	@PostMapping("/rest/credit")
	public CreditTransactionResponseDTO processCreditTransaction(@RequestBody CreditTransactionRequestDTO creditTransaction) {
		CreditTransactionResponseDTO response = new CreditTransactionResponseDTO();
		logger.info("Credit transacion request received.");
		Long accountId = Long.parseLong(creditTransaction.getAccount());
		String responseCode = transactionAuthorizationService.processTransaction(accountId, creditTransaction.getTotalAmount(), 
				creditTransaction.getMcc(), creditTransaction.getMerchant());
		response.setCode(responseCode);
		logger.info("Credit transacion processed for [ACCOUNT] " + creditTransaction.getAccount() + " [RESPONSE CODE] " + responseCode);
		return response;
	}
	
	@ExceptionHandler
    public CreditTransactionResponseDTO handleException(Exception ex) {
        logger.error("An exception has occurred while processing request.", ex);
        CreditTransactionResponseDTO response = new CreditTransactionResponseDTO();
        response.setCode(TransactionResponseCode.ERROR);
        return response;
    }
}
