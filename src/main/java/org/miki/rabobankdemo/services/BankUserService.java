package org.miki.rabobankdemo.services;

import java.util.List;
import java.util.Optional;

import org.miki.rabobankdemo.dto.BankUserDTO;
import org.miki.rabobankdemo.dto.CreateBankUserDTO;
import org.miki.rabobankdemo.models.BankUser;

public interface BankUserService {
	
    List <BankUser> findAll();
    
    BankUser create(CreateBankUserDTO newUser);

    BankUserDTO save(BankUserDTO user);
    
    BankUser save(BankUser user);

    Optional<BankUser> findById(Long id);
    
    BankUser getExistingUserById(long userId);
    
    Optional<BankUser> findByUserLogin(String userLogin);

    void delete(long id);

}