package org.miki.rabobankdemo.repositories;

import org.miki.rabobankdemo.models.AccountTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for account transaction history
 * @author Mikołaj Maciejewski
 *
 */
public interface AccountTransactionHistoryRepository  extends JpaRepository<AccountTransactionHistory, Long> {

}
