package org.miki.rabobankdemo.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity(name="BANK_USER")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
/**
 * Class representing User entity. Basic data about user
 * 
 * @author Mikołaj Maciejewski
 *
 */
public class BankUser {
    
	// fields
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Column(name="user_login",length = 32)
	@NotNull
	String userLogin;
	
	@Column(name="first_name",length = 25)
	@NotNull
	String firstName;
	
	@Column(name="last_name",length = 25)
	@NotNull
	String lastName;
	
	@Column(length = 64)
	String email;
	
	@JsonIgnore
	@Column(length = 32)
	@NotNull
	String password;
	
    @JsonIgnore
    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<BankAccount> bankAccounts;
    

}