package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AccountTransactionHistoryDTO(Long id, 
										   OffsetDateTime transactionDate, 
										   String type,
										   String sourceIBAN,
										   String destinationIBAN,
										   String currencyCode,
										   BigDecimal transferValue) {}
