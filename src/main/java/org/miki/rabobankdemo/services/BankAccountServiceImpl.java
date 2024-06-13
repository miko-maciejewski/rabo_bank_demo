package org.miki.rabobankdemo.services;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import org.miki.rabobankdemo.exceptions.AccountNotFoundException;
import org.miki.rabobankdemo.exceptions.UserNotFoundException;
import org.miki.rabobankdemo.exceptions.WrongParmeterValue;
import org.miki.rabobankdemo.models.AccountTransactionHistory;
import org.miki.rabobankdemo.models.BankAccount;
import org.miki.rabobankdemo.models.BankUser;
import org.miki.rabobankdemo.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of service for Bank account 
 * @author Mikołaj Maciejewski
 *
 */
@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {
	
	// here are relations to repositories
	private final BankAccountRepository bankAccountRepository;
	
	// here are services
	@Autowired
	private BankUserService bankUserService;
	
	@Autowired
	private BankAccountHistoryService bankAccountHistoryService;
	
	/**
	 *  constructor to initialize the service
	 * @param bankAccountRepository - bank account repo bean
	 * @param accountHistoryRepository
	 * @param bankUserService - user service bean
	 */
	BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
		this.bankAccountRepository = bankAccountRepository;
	}

	/** 
	 * verify currency code - have to be not null, 3 letter and from currencies table if wrong then WrongParmeterValue is thrown
	 * 
	 * @param currencyCode - currency code to check
	 */
	private void verifyCurrency(String currencyCode) {
		if (currencyCode == null) {
			log.debug("Error while currency verification.");
			throw new WrongParmeterValue("Currency code is not provided.");
		}
		
		if (currencyCode.length() != 3) {
			throw new WrongParmeterValue("Currency code has to be exact 3 letter code.");
		}
		
		// check if the currency exist in available currencies list
		boolean containsCurrency = Currency.getAvailableCurrencies().stream().filter(curr -> curr.getCurrencyCode().equals(currencyCode)).count() > 0;
		
		if (!containsCurrency) {
			throw new WrongParmeterValue("Currency code has to be on allowed list");
		}
		
	}
	
	// creates account not in transaction 
	@Override
	public BankAccount createAccount(BankAccount newBankAccount) {

		// and save it to DB with relation 
		BankAccount storedBankAccount = bankAccountRepository.save(newBankAccount);	
		
		
		// now entry for bank account history
		
		AccountTransactionHistory newSourceAccountHistory = new AccountTransactionHistory(null, storedBankAccount.getCreateDate(), "Account creation",  null, storedBankAccount.getIban(), storedBankAccount.getCurrencyCode(), storedBankAccount.getBalance(), storedBankAccount);
		
		bankAccountHistoryService.save(newSourceAccountHistory);
		
		return storedBankAccount;
	}
	
	@Override
	public BankAccount saveAccount(BankAccount newBankAccount) {
		return bankAccountRepository.save(newBankAccount);	
	}
	
	/**
	 * Creates new account for user with given currency and initial amount on account 
	 */
	@Override
	@Transactional
	public BankAccount createAccount(Long userId, String currencyCode, BigDecimal initalAmount) {
		
		// check initalAmount - have to be non negative
		if (initalAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new WrongParmeterValue("Initial found amount have to be non negative value");
		}
		
		verifyCurrency(currencyCode);
		
		// validation - find existing user by ID
		BankUser user = bankUserService.getExistingUserById(userId);

		// create current time object for input parameter
		OffsetDateTime currentDateTime = OffsetDateTime.now();
	
		// generate UUID in place of IBAN to short implementation of demo :)
		UUID uuid_iban = UUID.randomUUID();
		
		// create next bank account 
		BankAccount newBankAccount = new BankAccount(null,
										currentDateTime,
										currentDateTime,
										uuid_iban.toString(),
										currencyCode, 
										initalAmount, 
										user,
										Collections.emptyList());
		
		// create account and return stored instance
		return createAccount(newBankAccount);
	}
	
	/**
	 * Finds account by its ID. If account not found then exception is thrown
	 * @param accountId - id of account to find. 
	 * 
	 */
	@Override
	public BankAccount findExistingAccountByID(Long accountId) {
		
		return bankAccountRepository.findById(accountId).orElseThrow(() ->  new AccountNotFoundException("Account with id = [" + accountId + "] not found"));
	
	}

	/**
	 * finds list of accounts for user with given ID
	 * 
	 * @param - userId - id of user to find accounts for - exception UserNotFoundException thrown if user not found
	 * @return - list of accounts 
	 */
	@Override
	public List<BankAccount> findAccountsForUser(Long userId) {
		
		// validation - try find user by given user id - if not found - throw exception for controller
		BankUser user = bankUserService.findById(userId).orElseThrow(() ->  new UserNotFoundException("User with id = [" + userId + "] not found"));
		
		// return list of accounts for user		
		return user.getBankAccounts();
	}

	/**
	 * Provides user transction to transfer cash to user account 
	 * 
	 * @param userId - id of user to transfer cash for
	 * @param destAccountIban - id of account to transfer cash to - have to be existing account owed by user and same currency
	 * @param currencyCode - currency code of transferred money - have be same as account currency
	 * @param amount - amount of cash to transfer to account have to be value greater than 0
	 * 
	 * @return - bank account after transfer
	 */
	@Override
	@Transactional
	public BankAccount transferCashToAccount(Long userId, String destAccountIban, String currencyCode, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new WrongParmeterValue("The amount for cash transfer greater than 0.");
		}
		
		// read existing user
		BankUser user = bankUserService.getExistingUserById(userId);
		
		// try find account by ItsIBAN
		BankAccount destAccount = bankAccountRepository.findByIban(destAccountIban).orElseThrow(() -> new AccountNotFoundException("Account with IBAN id =[" + destAccountIban + "] not found" ));
	
		// check currency CODE - have to match account and transfer currency
		if (!destAccount.getCurrencyCode().equals(currencyCode)) {
			throw new WrongParmeterValue("The account and transfer currency codes does not match");
		}
		
		// check if user is owner of the account - he can transfer cash only to his own account
		if (!destAccount.getUser().getId().equals(user.getId())) {
			throw new WrongParmeterValue("The account is not owed by provided user");
		}
		
		// create current time object for input parameter
		OffsetDateTime currentDateTime = OffsetDateTime.now();
		
		// change balance of the account 
		BigDecimal newAmount = destAccount.getBalance().add(amount);
		destAccount.setBalance(newAmount);
		destAccount.setModificationDate(currentDateTime);
		
		// now save bank account
		BankAccount storedDestAccount = saveAccount(destAccount);
		// now entry for bank account history
		AccountTransactionHistory newAccountHistory = new AccountTransactionHistory(null, currentDateTime, "transfer cash", null, storedDestAccount.getIban(), currencyCode, amount, storedDestAccount);
		
		bankAccountHistoryService.save(newAccountHistory);
		// now entry for bank account history
		return storedDestAccount;
	}

	@Override
	@Transactional
	public List<BankAccount> transferBetweenAccounts(Long userId, 
													String srcAccountIBAN, 
													String destAccountIBAN,
													String currencyCode, 
													BigDecimal amount) {
		// check if amount is positve number
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new WrongParmeterValue("The amount for cash transfer greater than 0.");
		}
		
		// check if source and destination account number are the same
		if (srcAccountIBAN.equals(destAccountIBAN)) {
			throw new WrongParmeterValue("Source and Destination IBAN are equal.");
		}
		
		// read existing user
		BankUser user = bankUserService.getExistingUserById(userId);
		
		// try find source account by IBAN 
		BankAccount srcAccount = bankAccountRepository.findByIban(srcAccountIBAN).orElseThrow(() -> new AccountNotFoundException("Source account with IBAN id =[" + srcAccountIBAN + "] not found" ));
	
		// try find destination account by IBAN 
		BankAccount destAccount = bankAccountRepository.findByIban(destAccountIBAN).orElseThrow(() -> new AccountNotFoundException("Destination account with IBAN id =[" + destAccountIBAN + "] not found" ));
		
		
		// check currency CODE - have to match account and transfer currency
		if (!srcAccount.getCurrencyCode().equals(destAccount.getCurrencyCode())) {
			throw new WrongParmeterValue("The account and transfer accounts currency codes does not match");
		}
		
		// check if user is owner of the account - he can transfer cash only to his own account
		
		// check for destination account
		if (!srcAccount.getUser().getId().equals(user.getId())) {
			throw new WrongParmeterValue("The source account is not owed by provided user");
		}
		
		// check for destination account
		if (!destAccount.getUser().getId().equals(user.getId())) {
			throw new WrongParmeterValue("The destination account is not owed by provided user");
		}
		
		// check account balance is enough
		if (srcAccount.getBalance().compareTo(amount) < 0) {
			throw new WrongParmeterValue("The source account has less money then requested amount");
		}
			
		// create current timestamp
		OffsetDateTime currentDateTime = OffsetDateTime.now();
		
		// change balance of the account 
		BigDecimal newDestAmount = destAccount.getBalance().add(amount);
		BigDecimal newSrcAmount = srcAccount.getBalance().subtract(amount);
		
		// change of amount on accounts and account modification timestamps
		srcAccount.setBalance(newSrcAmount);
		srcAccount.setModificationDate(currentDateTime);
		destAccount.setBalance(newDestAmount);
		destAccount.setModificationDate(currentDateTime);
		
		// now save bank accounts
		// source
		BankAccount storedSrcAccount =  bankAccountRepository.save(srcAccount);
		
		// destination
		BankAccount storedDestAccount =  bankAccountRepository.save(destAccount);
		
		// now entry for bank account history
		
		AccountTransactionHistory newSourceAccountHistory = new AccountTransactionHistory(null, currentDateTime, "Money transfer to account", storedSrcAccount.getIban(), storedDestAccount.getIban(), currencyCode, amount, storedSrcAccount);
		
		bankAccountHistoryService.save(newSourceAccountHistory);
		
		// destination
		AccountTransactionHistory newDestAccountHistory = new AccountTransactionHistory(null, currentDateTime, "Money transfer to account", storedSrcAccount.getIban(), storedDestAccount.getIban(), currencyCode, amount, storedDestAccount);
		
		bankAccountHistoryService.save(newDestAccountHistory);
		return List.of(storedSrcAccount, storedDestAccount);
	}

	
	/**
	 * Provides user transaction to withdraw cash from user account 
	 * 
	 * @param userId - id of user to withdraw cash for
	 * @param srcAccountIBAN - id of account to withdraw cash from - have to be existing account owed by user and same currency
	 * @param currencyCode - currency code of withdrawn money - have be same as account currency
	 * @param amount - amount of cash to withdraw from account have to be value greater than 0
	 * 
	 * @return - bank account after withdrawn
	 */
	@Override
	@Transactional
	public BankAccount withdrawCashFromAccount(Long userId, String srcAccountIBAN, String currencyCode, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new WrongParmeterValue("The amount for cash withdraw has to be greater than 0.");
		}
		
		// read existing user
		BankUser user = bankUserService.getExistingUserById(userId);
		
		// try find account by srcAccountIBAN
		BankAccount srcAccount = bankAccountRepository.findByIban(srcAccountIBAN).orElseThrow(() -> new AccountNotFoundException("Account with IBAN id =[" + srcAccountIBAN + "] not found" ));
	
		// check currency CODE - have to match account and withdrawak currency
		if (!srcAccount.getCurrencyCode().equals(currencyCode)) {
			throw new WrongParmeterValue("The account and withdraw currency codes does not match");
		}
		
		// check if user is owner of the account - he can transfer cash only to his own account
		if (!srcAccount.getUser().getId().equals(user.getId())) {
			throw new WrongParmeterValue("The account is not owed by provided user");
		}
		
		// check account balance is enough
		if (srcAccount.getBalance().compareTo(amount) < 0) {
			throw new WrongParmeterValue("The source account has less money then requested amount");
		}
		
		// create current time object for input parameter
		OffsetDateTime currentDateTime = OffsetDateTime.now();
		
		// change balance of the account 
		BigDecimal newAmount = srcAccount.getBalance().subtract(amount); // @TODO set rounding mode in future
		srcAccount.setBalance(newAmount);
		srcAccount.setModificationDate(currentDateTime);
		
		// now save bank account
		BankAccount storedAccount =  bankAccountRepository.save(srcAccount);
		
		// now entry for bank account history
		AccountTransactionHistory newAccountHistory = new AccountTransactionHistory(null, currentDateTime, "Money withdraw", storedAccount.getIban(), null, currencyCode, amount, storedAccount);
		
		// save history in repository
		bankAccountHistoryService.save(newAccountHistory);

		return storedAccount;
	}
}
