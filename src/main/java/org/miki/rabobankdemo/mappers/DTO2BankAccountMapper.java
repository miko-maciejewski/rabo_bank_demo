package org.miki.rabobankdemo.mappers;

import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankAccountDTO;
import org.miki.rabobankdemo.models.BankAccount;
import org.springframework.stereotype.Service;

@Service
public class DTO2BankAccountMapper implements Function<BankAccountDTO, BankAccount> {
	
	@Override
	public BankAccount apply(BankAccountDTO baDTO) {
		
		return new BankAccount(baDTO.id(),
							   baDTO.createDate(), 
							   baDTO.modificationDate(), 
							   baDTO.iban(), 
							   baDTO.currencyCode(), 
							   baDTO.balance()
							   , null // @TODO: update relation to user
							   , null
								); 
	}

}