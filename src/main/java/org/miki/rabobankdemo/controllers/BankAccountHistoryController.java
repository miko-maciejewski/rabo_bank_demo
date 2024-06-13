package org.miki.rabobankdemo.controllers;

import java.util.List;

import org.miki.rabobankdemo.dto.AccountTransactionHistoryDTO;
import org.miki.rabobankdemo.mappers.AccountTransactionHistoryList2DTOMapper;
import org.miki.rabobankdemo.models.AccountTransactionHistory;
import org.miki.rabobankdemo.services.BankAccountHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for account history endpoints
 * @author Mikołaj Maciejewski
 *
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class BankAccountHistoryController {

	
    private final BankAccountHistoryService bankAccountHistoryService;
    
    private final AccountTransactionHistoryList2DTOMapper accHistoryList2DTOMapper;
    
    /**
     * Constructor for service with services reference for init
     * 
     * @param bankAccountHistoryService
     * @param accHistoryList2DTOMapper
     */
    BankAccountHistoryController(BankAccountHistoryService bankAccountHistoryService, AccountTransactionHistoryList2DTOMapper accHistoryList2DTOMapper) {
    	this.bankAccountHistoryService = bankAccountHistoryService;
    	this.accHistoryList2DTOMapper = accHistoryList2DTOMapper;
    }
	
	/**
	 * Returns list of whole bank account history (operations)
	 * 
	 * @return - List of bank account history in DTO
	 */
	@GetMapping("/accountHistory/{accountId}")
	public List<AccountTransactionHistoryDTO> getAccountHistory(@PathVariable("accountId") Long accountId) {
		
		log.info("Controller - call find all account history entries for account.");
		// read account history list and map to list of account history DTO
		try {
			List<AccountTransactionHistory> accountHistoryList = bankAccountHistoryService.findAccountHistory(accountId);
			
			return  accHistoryList2DTOMapper.apply(accountHistoryList);
		} catch (Exception e) {
			// any other exception
			log.error("Error while retrive account history list for account id = {}",  accountId, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", e);
		}
		
	}

}
