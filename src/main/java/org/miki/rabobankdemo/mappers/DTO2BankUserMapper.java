package org.miki.rabobankdemo.mappers;

import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankUserDTO;
import org.miki.rabobankdemo.models.BankUser;
import org.springframework.stereotype.Service;

@Service
public class DTO2BankUserMapper implements Function<BankUserDTO, BankUser> {
	
	@Override
	public BankUser apply(BankUserDTO useerDTO2map) {
		
		return new BankUser(useerDTO2map.id(),
						useerDTO2map.userLogin(),
						useerDTO2map.firstName(),
						useerDTO2map.lastName(),
						useerDTO2map.email(), 
						null, 
						null
						);
	}
	
}