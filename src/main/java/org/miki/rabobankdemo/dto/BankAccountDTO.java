package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record BankAccountDTO(Long id, 
							 OffsetDateTime createDate, 
							 OffsetDateTime modificationDate, 
							 String iban, 
							 String currencyCode,
							 BigDecimal balance) {}
