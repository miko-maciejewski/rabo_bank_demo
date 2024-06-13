package org.miki.rabobankdemo.dto;

import java.math.BigDecimal;

/**
 * DTO object for create user with inital account
 * @author Mikołaj Maciejewski
 * 
 * String userLogin - login of new created user - have to be unique within DB 
 * String firstName - first name of user
 * String lastName - last name of user
 * String email - email of user
 * String currencyCode - currency code of the account
 * BigDecimal balance - initial balance of the account
 */

public record CreateBankUserDTO(String userLogin, String firstName, String lastName, String email, String password,
			String currencyCode, BigDecimal balance) { 
}
