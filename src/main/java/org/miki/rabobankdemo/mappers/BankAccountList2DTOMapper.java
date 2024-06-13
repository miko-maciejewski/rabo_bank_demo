package org.miki.rabobankdemo.mappers;

import java.util.List;
import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankAccountDTO;
import org.miki.rabobankdemo.models.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/** 
 * Definiton of service responsible for mapping BankAccount to BankAccountDTO (Data Transfer Object)
 * 
 * @author Mikołaj Maciejewski
 *
 */
@Service
public class BankAccountList2DTOMapper implements Function<List<BankAccount>, List<BankAccountDTO>> { 

	/**
	 * Reference to bank account -> DTO Mapper (for single entry)
	 */
	@Autowired
	private BankAccount2DTOMapper bankAccount2DTOMapper;
	
	/**
	 * Definition of mapper of list of DTOs to list of Bank Account
	 */
	@Override
	public List<BankAccountDTO> apply(List<BankAccount> bankAccounts) {
		
		// stream processing of every bank account to
		return bankAccounts
				.stream()
				.map(bankAccount2DTOMapper)
				.toList();
	};

}
