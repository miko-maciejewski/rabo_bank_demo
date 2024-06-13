package org.miki.rabobankdemo.mappers;

import java.util.List;
import java.util.function.Function;

import org.miki.rabobankdemo.dto.BankUserDTO;
import org.miki.rabobankdemo.models.BankUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankUserList2DTOMapper implements Function<List<BankUser>, List<BankUserDTO>> { 

	/**
	 * Reference to user -> DTO Mapper (for single user entry)
	 */
	@Autowired
	private BankUser2DTOMapper user2DTOMapper;

	@Override
	public List<BankUserDTO> apply(List<BankUser> userList2map) {

		return userList2map.stream().map(user2DTOMapper).toList();
	}

}
