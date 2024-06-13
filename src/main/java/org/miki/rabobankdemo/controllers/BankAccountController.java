package org.miki.rabobankdemo.controllers;


import java.util.List;

import org.miki.rabobankdemo.dto.BankAccountDTO;
import org.miki.rabobankdemo.dto.CreateBankAccountDTO;
import org.miki.rabobankdemo.dto.TransferCashToAccountDTO;
import org.miki.rabobankdemo.dto.WithdrawCashDTO;
import org.miki.rabobankdemo.dto.TransferBetweenAccountsDTO;
import org.miki.rabobankdemo.exceptions.AccountNotFoundException;
import org.miki.rabobankdemo.exceptions.UserNotFoundException;
import org.miki.rabobankdemo.exceptions.WrongParmeterValue;
import org.miki.rabobankdemo.mappers.BankAccount2DTOMapper;
import org.miki.rabobankdemo.mappers.BankAccountList2DTOMapper;
import org.miki.rabobankdemo.models.BankAccount;
import org.miki.rabobankdemo.services.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller delivering REST interface for Users processing
 * 
 * @author Mikołaj Maciejewski
 *
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class BankAccountController {

	private final BankAccountService accountService;
	
	private final BankAccount2DTOMapper baAccount2dtoMapper;
	
	private final BankAccountList2DTOMapper baAccountList2DTOMapper;

	/** 
	 * Construct controller for BankUser and populate fields with reference to services
	 * @param userService
	 * @param user2DTOMapper
	 * @param userList2DTOMapper
	 */
	public BankAccountController(BankAccountService accountService, 
							  BankAccount2DTOMapper baAccount2dtoMapper,
							  BankAccountList2DTOMapper baAccountList2DTOMapper) {
		this.accountService = accountService;
		this.baAccount2dtoMapper = baAccount2dtoMapper;
		this.baAccountList2DTOMapper = baAccountList2DTOMapper;
	}
	
	/**
	 * Returns list of all accounts for user in db
	 * 
	 * @return - List of bank accounts in DTO
	 */
	@GetMapping("/accounts/{userId}")
	public List<BankAccountDTO> getAccounts(@PathVariable("userId") Long userID) {
		
		List<BankAccount> accountList = null;
		log.info("Controller - call find all accounts for user.");
		// read accounts and map to account DTO
		try {
			accountList = accountService.findAccountsForUser(userID);
		} catch (Exception e) {
			// any other exception
			log.error("Error while retrive user list for user id = {}",  userID, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", e);
		}
		return baAccountList2DTOMapper.apply(accountList);
	}

	/**
	 * Finds account by ID in db
	 * 
	 * @param accountId - account ID of account to find
	 * 
	 * @return  - account 
	 * 
	 */
	@GetMapping("/account/{accountId}")
	public BankAccountDTO getAccountById(@PathVariable("accountId") Long accountID) {
		try {
			return baAccount2dtoMapper.apply(accountService.findExistingAccountByID(accountID));

		} catch (AccountNotFoundException anf) {
			log.debug("Account with id = {} not found",  accountID, anf);
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Account not found while search", anf);

		} catch (Exception e) {
			// any other exception
			log.error("Error while retrive user list for user id = {}",  accountID, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error search account by ID", e);
		}				
	}

	
	/**
	 * Endpoint for bank account creation 
	 * @param bankAccountData - parameters for new bank account 
	 * @return - created BankAccount
	 */
	@PostMapping("/account")
	public BankAccountDTO createAccount(@RequestBody CreateBankAccountDTO bankAccountData) {
		// public BankAccount createAccount(Long userId, String currencyCode, BigDecimal initalAmount) {
	  try {
		  BankAccount newBankAccount = accountService.createAccount(bankAccountData.userId(),bankAccountData.currencyCode(), bankAccountData.initalAmount());
		  return baAccount2dtoMapper.apply(newBankAccount);
		} catch (Exception e) {
			// any other exception
			log.error("Internal server error while account creation for {}",  bankAccountData, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error while account creation.", e);
		}	
	}
	
	/**
	 * Endpoint for transfering cash to account. 
	 * 
	 * @param userId - id of user that transfering cash to account
	 * @param destAccountIban - IBAN of account to transfer data to
	 * @param currencyCode - currency to transfer - have to be consistent with account currency
	 * @param amount - amount to transfer to accouint
	 * @return - account after transaction
	 */
	@PutMapping("/transferCashToAccount")
	public BankAccountDTO transferCashToAccount(@RequestBody TransferCashToAccountDTO transferData) {
		try {
		 BankAccount storedBankAccount =  accountService.transferCashToAccount(transferData.userId(), transferData.destAccountIban(), transferData.currencyCode(), transferData.amount());
		 return baAccount2dtoMapper.apply(storedBankAccount);
		 
		} catch (UserNotFoundException une) {
            // If user not exists
            log.error("User with id {} does not exists", transferData.userId());
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "User not exists. Transfer failed", une);
        } catch (WrongParmeterValue wpe) {
            // If user not exists
            log.error("Wrong paramerers for cash transfer {}", transferData);
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Wrong parameter for cash transfer " + transferData + " Transfer failed", wpe);		 
		} catch (Exception e) {
			// any other exception
			log.error("Internal server error while cash transfer for {}",  transferData, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error while cash transfer.", e);
		}	 
	}

	/**
	 * Endpoint for transfer money between accounts of customer
	 * 
	 * @param transferData - DTO with data for transfer
	 * @return - list of update accounts after transfer
	 */
	@PutMapping("/transferBetweenAccounts")
	public List<BankAccountDTO> transferBetweenAccounts(@RequestBody TransferBetweenAccountsDTO transferData) {
		try {
			List<BankAccount> storedAccounts = accountService.transferBetweenAccounts(transferData.userId(), 
													transferData.srcAccountIBAN(), 
													transferData.destAccountIBAN(), 
													transferData.currencyCode(), 
													transferData.amount());
			return baAccountList2DTOMapper.apply(storedAccounts);
		} catch (UserNotFoundException une) {
            // If user not exists
            log.error("User with id {} does not exists", transferData.userId());
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "User not exists. Transfer failed", une);
        } catch (WrongParmeterValue wpe) {
            // If user not exists
            log.error("Wrong paramerers for transfer {}", transferData);
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Wrong parameter for transfer " + transferData + " Transfer failed", wpe);		 
		} catch (Exception e) {
			// any other exception
			log.error("Internal server error while transfer for {}",  transferData, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error while transfer.", e);
		}	
		
	}
	
	/**
	 * Endpoint for cash withdrawal from user account
	 * 
	 * @param withdrawCashData - dataset for cash withdraw from account
	 * @return account after withdrawal
	 */
	@PutMapping("/withdrawFromAccount")
	public BankAccountDTO withdrawCashFromAccount(@RequestBody WithdrawCashDTO withdrawCashData) {
		try {
			 BankAccount storedBankAccount =  accountService.withdrawCashFromAccount(withdrawCashData.userId(), withdrawCashData.srcAccountIBAN(), withdrawCashData.currencyCode(), withdrawCashData.amount());
			 return baAccount2dtoMapper.apply(storedBankAccount);
			 
			} catch (UserNotFoundException une) {
	            // If user not exists
	            log.error("User with id {} does not exists", withdrawCashData.userId());
	            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "User not exists. Cash withdraw failed", une);
	        } catch (WrongParmeterValue wpe) {
	            // If user not exists
	            log.error("Wrong paramerers for cash transfer {}, Problem: {}", withdrawCashData, wpe.getMessage());
	            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Wrong parameter for cash withdraw [" + withdrawCashData + "]. Cash withdraw failed", wpe);		 
			} catch (Exception e) {
				// any other exception
				log.error("Internal server error while cash transfer for {}",  withdrawCashData, e);
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error while cash transfer.", e);
			}			
	}	
}