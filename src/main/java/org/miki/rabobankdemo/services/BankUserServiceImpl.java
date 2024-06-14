package org.miki.rabobankdemo.services;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.mapping.Collection;
import org.miki.rabobankdemo.dto.BankAccountDTO;
import org.miki.rabobankdemo.dto.BankUserDTO;
import org.miki.rabobankdemo.dto.CreateBankUserDTO;
import org.miki.rabobankdemo.exceptions.UserAlreadyExistsException;
import org.miki.rabobankdemo.exceptions.UserNotFoundException;
import org.miki.rabobankdemo.mappers.DTO2BankUserMapper;
import org.miki.rabobankdemo.models.BankAccount;
import org.miki.rabobankdemo.models.BankUser;
import org.miki.rabobankdemo.repositories.BankAccountRepository;
import org.miki.rabobankdemo.repositories.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankUserServiceImpl implements BankUserService {
	
	@Autowired
	private BankUserRepository userRepository;
	
	@Autowired
	private BankAccountService accountService;
	
	@Autowired
	private DTO2BankUserMapper dto2BankUserMapper;
	
	
	@Override
	public List<BankUser> findAll() {
		log.info("Find all user called");
		return userRepository.findAll();
	}

	/**
	 * Updates user data  (TODO)
	 */
	@Override
	public BankUserDTO save(BankUserDTO userDto) {
		BankUser newUserData = dto2BankUserMapper.apply(userDto);
		
		Optional<BankUser> dbUserData = userRepository.findById(newUserData.getId());
	/*	.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with login={" + userLogin + "} not found.")
					)
		*/
		return null;
	}
	
	@Override
	@Transactional
	public BankUser create(CreateBankUserDTO newUser) {
		
		// first check if user with same login exists - it is unique for users
		Optional<BankUser> dbUserData = findByUserLogin(newUser.userLogin());
		
		// throw UserAlreadyExistsException if there is conflict
		if (dbUserData.isPresent()) new UserAlreadyExistsException("User creation login conflict - login already exists");
		
		// create current time object for input parameter
		OffsetDateTime currentDateTime = OffsetDateTime.now();
		
		BankUser newUserDB = new BankUser(null, 
										newUser.userLogin(), 
										newUser.firstName(), 
										newUser.lastName(), 
										newUser.email(),
										newUser.password(), 
										Collections.emptyList() 
									);

		
		// generate UUID in place of IBAN to short implementation of demo :)
		UUID uuid_iban = UUID.randomUUID();

		
		// create inital bank account 
		BankAccount newBankAccount = new BankAccount(null,
										currentDateTime,
										currentDateTime,
										uuid_iban.toString(),
										newUser.currencyCode(), 
										newUser.balance(), 
										newUserDB,
										Collections.emptyList());
		
		// store user
		BankUser savedUserDB = userRepository.save(newUserDB);

		// and save it to DB with relation 
		BankAccount savedBankAccount = accountService.createAccount(newBankAccount);
		
		savedUserDB.setBankAccounts(List.of(savedBankAccount));

		BankUser userWithAccount = userRepository.findById(savedUserDB.getId()).get();
		
		// return the new user
		return savedUserDB;
	}

	@Override
	public BankUser save(BankUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Finds Bank user by its ID
	 * @param id - id of the user
	 * @return - Optional with BankUser using ID
	 */
	@Override
	public Optional<BankUser> findById(Long id) {
		return userRepository.findById(id);
	}
	
	
	/**
	 * Try load user by its userID - if user not exists it throws exception UserNotFoundException
	 * @param userId - id of user to find
	 * @return existing user instance
	 */
	public BankUser getExistingUserById(long userId) {
		// validation - check if user by given user id exists - if not found - throw exception for controller
		return findById(userId).orElseThrow(() ->  new UserNotFoundException("User with id = [" + userId + "] not found"));
	} 	

	/**
	 * Finds Bank User by its login
	 * @param userLogin - login of user to find
	 * @return Optional with BankUser found by login
	 */
	@Override
	public Optional<BankUser> findByUserLogin(String userLogin) {
		return userRepository.findByUserLogin(userLogin);
	}

	@Override
	public void delete(long id) {
		// for demo - not implemented
		
	}
	
	
}