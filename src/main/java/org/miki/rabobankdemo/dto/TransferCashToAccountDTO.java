package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;

public record TransferCashToAccountDTO(Long userId, String destAccountIban, String currencyCode, BigDecimal amount) {

}
