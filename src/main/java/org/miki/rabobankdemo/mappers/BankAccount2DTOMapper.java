package org.miki.rabobankdemo.mappers;

import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankAccountDTO;
import org.miki.rabobankdemo.models.BankAccount;
import org.springframework.stereotype.Service;

/**
 * Helper class - mapper responsible for mapping BankAccount entity to bank account DTO.
 * It implements functional interface for mapping
 * 
 * @author Mikołaj Maciejewski
 *
 */
@Service
public class BankAccount2DTOMapper implements Function<BankAccount, BankAccountDTO> {
	
	@Override
	public BankAccountDTO apply(BankAccount bankAccount) {
		
		return new BankAccountDTO(bankAccount.getId(),
								  bankAccount.getCreateDate(),
								  bankAccount.getModificationDate(),
								  bankAccount.getIban(),
								  bankAccount.getCurrencyCode(),
								  bankAccount.getBalance());
	};
};
