package org.miki.rabobankdemo.repositories;

import java.util.Optional;

import org.miki.rabobankdemo.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for bank accounts
 * @author Mikołaj Maciejewski
 *
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
	Optional<BankAccount> findByIban(String iban);
	
}
