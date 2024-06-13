package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;

public record CreateBankAccountDTO(Long userId, String currencyCode, BigDecimal initalAmount) {

}
