package org.miki.rabobankdemo.mappers;

import java.util.List;
import java.util.function.Function;

import org.miki.rabobankdemo.dto.AccountTransactionHistoryDTO;
import org.miki.rabobankdemo.models.AccountTransactionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountTransactionHistoryList2DTOMapper implements Function<List<AccountTransactionHistory>, List<AccountTransactionHistoryDTO>> { 

	/**
	 * Reference to account history -> DTO Mapper (for single account history  entry)
	 */
	@Autowired
	private AccountTransactionHistory2DTOMapper accHistory2DTOMapper;

	@Override
	public List<AccountTransactionHistoryDTO> apply(List<AccountTransactionHistory> accHistoryList2map) {

		return accHistoryList2map.stream().map(accHistory2DTOMapper).toList();
	}
	

}
