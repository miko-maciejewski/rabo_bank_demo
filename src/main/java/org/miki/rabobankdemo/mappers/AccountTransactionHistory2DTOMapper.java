package org.miki.rabobankdemo.mappers;

import java.util.function.Function;

import org.miki.rabobankdemo.dto.AccountTransactionHistoryDTO;
import org.miki.rabobankdemo.models.AccountTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class AccountTransactionHistory2DTOMapper implements Function<AccountTransactionHistory, AccountTransactionHistoryDTO> { 

	
	@Override
	public AccountTransactionHistoryDTO apply(AccountTransactionHistory bankAccountHistory) {
		
		return new AccountTransactionHistoryDTO(bankAccountHistory.getId(),
												  bankAccountHistory.getTransactionDate(),
												  bankAccountHistory.getType(),
												  bankAccountHistory.getSourceIBAN(),
												  bankAccountHistory.getDestinationIBAN(),
												  bankAccountHistory.getCurrencyCode(),
												  bankAccountHistory.getTransferValue());
	};
}
