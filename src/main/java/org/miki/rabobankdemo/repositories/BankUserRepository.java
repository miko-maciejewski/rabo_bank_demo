package org.miki.rabobankdemo.repositories;

import java.util.Optional;

import org.miki.rabobankdemo.models.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for users
 * @author Mikołaj Maciejewski
 *
 */
@Repository
public interface BankUserRepository extends JpaRepository<BankUser, Long> {
	Optional<BankUser> findByUserLogin(String userLogin);
}
