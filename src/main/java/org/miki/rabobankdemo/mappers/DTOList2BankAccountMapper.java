package org.miki.rabobankdemo.mappers;

import java.util.List;
import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankAccountDTO;
import org.miki.rabobankdemo.models.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Definition of service responsible for mapping List of bank account DTOs into BankAccount enitity
 * 
 * @author Mikołaj Maciejewski
 *
 */
@Service
public class DTOList2BankAccountMapper implements Function<List<BankAccountDTO>, List<BankAccount>> { 

	// refrence to DTO -> BankAccount mapper
	@Autowired
	private DTO2BankAccountMapper DTO2BankAccountMapper;
	
	/**
	 * Definition of mapping function
	 */
	@Override
	public List<BankAccount> apply(List<BankAccountDTO> bankAccountDTOs) {
		
		return bankAccountDTOs
				.stream()
				.map(DTO2BankAccountMapper)
				.toList();
	
	};
	
};
