package org.miki.rabobankdemo.services;

import java.util.List;

import org.miki.rabobankdemo.models.AccountTransactionHistory;
import org.miki.rabobankdemo.models.BankAccount;
import org.miki.rabobankdemo.repositories.AccountTransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountHistoryServiceImpl implements BankAccountHistoryService {
	
	// refrences 
	
	// to services
	@Autowired
	private BankAccountService accountService;
	
	// to repositories
	private final AccountTransactionHistoryRepository accountHistoryRepository;

	BankAccountHistoryServiceImpl(AccountTransactionHistoryRepository accountHistoryRepository) {

		this.accountHistoryRepository = accountHistoryRepository;

	}
	
	/**
	 * provides account transaction history list by accountId
	 * @param accountId - id of account to find history for 
	 * 
	 * @return - list of transaction on account
	 */
	@Override
	public List<AccountTransactionHistory> findAccountHistory(Long accountId) {

		BankAccount bankAccount = accountService.findExistingAccountByID(accountId);								  
		
		return bankAccount.getAccountHistory();
	}
	
	
	/**
	 * Saves account history to DB
	 * @param accountHistory - account history instance to store
	 * 
	 * @return stored account history instance
	 */
	public AccountTransactionHistory save(AccountTransactionHistory accountHistory) {
		return this.accountHistoryRepository.save(accountHistory);
	}
	
}
