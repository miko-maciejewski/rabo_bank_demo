package org.miki.rabobankdemo.mappers;

import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankUserDTO;
import org.miki.rabobankdemo.models.BankUser;
import org.springframework.stereotype.Service;

@Service
public class BankUser2DTOMapper implements Function<BankUser, BankUserDTO> {
	
	@Override
	public BankUserDTO apply(BankUser user2map) {
		
		return new BankUserDTO(user2map.getId(),
							user2map.getUserLogin(),
							user2map.getFirstName(),
							user2map.getLastName(),
							user2map.getEmail());
	}
	
}