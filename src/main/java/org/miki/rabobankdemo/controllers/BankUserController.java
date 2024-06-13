package org.miki.rabobankdemo.controllers;


import java.util.List;
import org.miki.rabobankdemo.dto.BankUserDTO;
import org.miki.rabobankdemo.dto.CreateBankUserDTO;
import org.miki.rabobankdemo.exceptions.UserAlreadyExistsException;
import org.miki.rabobankdemo.mappers.BankUser2DTOMapper;
import org.miki.rabobankdemo.mappers.BankUserList2DTOMapper;
import org.miki.rabobankdemo.services.BankUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller delivering REST interface for Users processing
 * 
 * @author Mikołaj Maciejewski
 *
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class BankUserController {

	private final BankUserService userService;

	private final BankUser2DTOMapper user2DTOMapper;

	private final BankUserList2DTOMapper userList2DTOMapper;

	/** 
	 * Construct controller for BankUser and populate fields with reference to services
	 * @param userService
	 * @param user2DTOMapper
	 * @param userList2DTOMapper
	 */
	public BankUserController(BankUserService userService, BankUser2DTOMapper user2DTOMapper, BankUserList2DTOMapper userList2DTOMapper) {
		this.userService = userService;
		this.user2DTOMapper = user2DTOMapper;
		this.userList2DTOMapper = userList2DTOMapper;
	}
	
	/**
	 * Returns list of all users in db (function for admin)
	 * 
	 * @return - List of BankUsers in DTO
	 */
	@GetMapping("/users")
	public List<BankUserDTO> getUsers() {
		log.info("Controller - call find all users.");
		return  userList2DTOMapper.apply(userService.findAll());
	}

	/**
	 * finds user by ID in db
	 * 
	 * @return BankUserDTO object with data
	 */
	@CrossOrigin
	@GetMapping("/user/{userId}")
	public BankUserDTO getUserById(@PathVariable("userId") Long id) {
		return user2DTOMapper.apply(
					userService
					.findById(id)
					.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id={" + id + "} not found.")
						)
					);
	}

	/**
	 * finds user by userLogin in db
	 * 
	 * @return BankUserDTO object with data found by userLogin
	 */
	@GetMapping("/userByLogin/{userLogin}")
	public BankUserDTO getUserByMail(@PathVariable("userLogin") String userLogin) {
		log.info("find user by login: {}", userLogin);
		
		return user2DTOMapper.apply(
				userService
				.findByUserLogin(userLogin)
				.orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with login={" + userLogin + "} not found.")
					)
				);
	}
	
	/**
	 * Endpoint for user creation.
	 * 
	 * @param user2Create - input parameter from Post Request
	 * 
	 * @return BankUserDTO object with new created user
	 */
	@PostMapping("/user")
	public BankUserDTO createUser(@RequestBody CreateBankUserDTO user2Create) {
		try {
			log.info("Create user with login: {}", user2Create.userLogin());
			
			var createdUser = userService.create(user2Create);
			
			return user2DTOMapper.apply(createdUser);
        } catch (UserAlreadyExistsException e) {
            // If user already exists, throw a 409 Conflict status
            log.error("User with login {} already exists", user2Create.userLogin(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists", e);			
		} catch (Exception e) {
			// any other exception
			log.error("Error creating user with login {}", user2Create.userLogin(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", e);
		}
		
	}	
}