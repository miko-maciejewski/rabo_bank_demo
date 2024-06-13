package org.miki.rabobankdemo.services;

import java.util.List;

import org.miki.rabobankdemo.models.AccountTransactionHistory;

public interface BankAccountHistoryService {

	// list of account transaction history
	List<AccountTransactionHistory> findAccountHistory(Long accountId);
	
	AccountTransactionHistory save(AccountTransactionHistory accountHistory);
	
}
