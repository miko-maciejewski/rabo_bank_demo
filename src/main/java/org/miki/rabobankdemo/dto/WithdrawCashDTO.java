package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;

public record WithdrawCashDTO(Long userId, String srcAccountIBAN, String currencyCode, BigDecimal amount) {

}
