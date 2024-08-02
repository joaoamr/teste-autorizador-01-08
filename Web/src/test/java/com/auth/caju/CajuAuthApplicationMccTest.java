package com.auth.caju;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.auth.caju.controller.CreditController;
import com.auth.caju.controller.dto.CreditTransactionRequestDTO;
import com.auth.caju.controller.dto.CreditTransactionResponseDTO;
import com.auth.caju.service.TransactionResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class CajuAuthApplicationMccTest {

	private static CreditTransactionResponseDTO successResponse = new CreditTransactionResponseDTO(
			TransactionResponseCode.SUCCESS);

	private static CreditTransactionResponseDTO deniedResponse = new CreditTransactionResponseDTO(
			TransactionResponseCode.DENIED);

	private MockMvc mockMvc;

	@Autowired
	private CreditController creditController;

	private ObjectMapper mapper = new ObjectMapper();

	@BeforeAll
	public void start() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(creditController).build();
	}

	@Test
	public void testMccFallback() throws JsonProcessingException, Exception {

		// Category = 0
		CreditTransactionRequestDTO request1 = new CreditTransactionRequestDTO("1", 100.0, "5811", null);

		// Must return OK
		mockMvc.perform(MockMvcRequestBuilders.post("/rest/credit").content(mapper.writeValueAsBytes(request1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(mapper.writeValueAsString(successResponse)));

		// Must deny
		mockMvc.perform(MockMvcRequestBuilders.post("/rest/credit").content(mapper.writeValueAsBytes(request1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(mapper.writeValueAsString(deniedResponse)));

	}
	
	@Test
	public void testMccCategoryWithFallback() throws JsonProcessingException, Exception {

		// Category = 1
		CreditTransactionRequestDTO request1 = new CreditTransactionRequestDTO("2", 100.0, "5411", null);

		// Must return OK
		for (int i = 0; i < 3; i++) {
			mockMvc.perform(MockMvcRequestBuilders.post("/rest/credit").content(mapper.writeValueAsBytes(request1))
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(successResponse)));
		}
		
		//Fallback
		for (int i = 0; i < 2; i++) {
			mockMvc.perform(MockMvcRequestBuilders.post("/rest/credit").content(mapper.writeValueAsBytes(request1))
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(successResponse)));
		}
		
		// Must deny
		mockMvc.perform(MockMvcRequestBuilders.post("/rest/credit").content(mapper.writeValueAsBytes(request1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(mapper.writeValueAsString(deniedResponse)));

	}
	
}
