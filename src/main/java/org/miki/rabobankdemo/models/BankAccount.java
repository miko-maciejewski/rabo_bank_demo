package org.miki.rabobankdemo.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="BANK_ACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "Training date is mandatory")
	@Column(name="create_date", nullable = false)
	private OffsetDateTime createDate;
	
	@NotNull(message = "Modification date is mandatory")
	@Column(name="modification_date", nullable = false)
	private OffsetDateTime modificationDate; // last modification date
	
	@NotNull(message = "Account IBAN Number is mandatory")
	@Column(name="iban", length = 50, unique = true)
	private String iban;
	
	// keeps 3 letter currency code
	@NotNull(message = "Account currency is mandatory")
	@Column(name="currency", length = 3)
	@Length(min=3, max = 3)
	private String currencyCode;
	
	// account balance in currency
	@NotNull(message = "Account balance is mandatory")
	@Column(name="balance", precision=18, scale = 4)
	private BigDecimal balance;
	
	@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private BankUser user;
	
    @OneToMany(mappedBy="account", fetch = FetchType.LAZY)
    private List<AccountTransactionHistory> accountHistory;
	
}
