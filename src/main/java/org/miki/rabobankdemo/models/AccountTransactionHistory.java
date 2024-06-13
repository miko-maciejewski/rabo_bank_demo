package org.miki.rabobankdemo.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="BANK_ACCOUNT_HISTORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
/****
 * Represents processed transaction
 * 
 * @author Mikołaj Maciejewski
 *
 */
public class AccountTransactionHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull(message = "Transaction date is mandatory")
	@Column(name="transaction_date", nullable = false)
	OffsetDateTime transactionDate;
	
	// transaction type:
	// - transfer of cash money  to account 
	// - transfer between existing internal accounts
	// - cash withdrawal from account (ie. by ATM)
	@NotNull(message = "Transction type is mandatory")
	@Column(name = "type", length=30, nullable = false)
	String type;	
	
	// source account iban - (for money transfer internal and external between accounts)
	// it may be null for money transfer to account
	@Column(name="src_acc_iban", length = 50, nullable = true)
	String sourceIBAN;
	
	// destination account iban
	@Column(name="dest_acc_iban", length = 50, nullable = true)
	String destinationIBAN;
	
	// 3 letter currency code
	@NotNull(message = "transfer currency is mandatory")
	@Column(name="iban", length = 3, nullable = false)
	@Length(min=3, max = 3)
	String currencyCode;
	
	// value of money transfer
	@NotNull(message = "Account balance is mandatory")
	@Column(name="transfer_value", precision=18, scale = 4, nullable = false)
	BigDecimal transferValue;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private BankAccount account;
}
