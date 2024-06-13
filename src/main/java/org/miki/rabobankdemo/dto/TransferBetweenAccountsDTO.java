package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;

public record TransferBetweenAccountsDTO(Long userId, String srcAccountIBAN, String destAccountIBAN,String currencyCode, BigDecimal amount) {

}
