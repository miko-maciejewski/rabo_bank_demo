package org.miki.rabobankdemo.services;

import java.math.BigDecimal;
import java.util.List;

import org.miki.rabobankdemo.models.BankAccount;

public interface BankAccountService {
	// finds account by its ID
	BankAccount findExistingAccountByID(Long accountId);
	
	//creates new account for user
	BankAccount createAccount(Long userId, String currencyCode, BigDecimal initalAmount); 
	
	// creates account not in transaction 
	BankAccount createAccount(BankAccount newBankAccount);
	
	// creates account not in transaction 
	BankAccount saveAccount(BankAccount newBankAccount);
	
	// provides list of accounts for user
	List<BankAccount> findAccountsForUser(Long userId);
	

	
	// transfer cash to account of the user - currency have be same as account currency
	BankAccount transferCashToAccount(Long userId, String destAccount, String currencyCode, BigDecimal amount);
	
	// transfers money between 2 accounts of user - if currency are same and there is enough amount of money on source money
	List<BankAccount> transferBetweenAccounts(Long userId,  String srcAccount, String destAccount, String currencyCode, BigDecimal amount);

	// withdraws cash from account of the user - if currency is same and available am
	BankAccount withdrawCashFromAccount(Long userId, String srcAccount, String currencyCode, BigDecimal amout);




	
}
