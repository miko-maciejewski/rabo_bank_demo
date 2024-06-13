package org.miki.rabobankdemo.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miki.rabobankdemo.dto.CreateBankUserDTO;
import org.miki.rabobankdemo.models.BankUser;
import org.miki.rabobankdemo.repositories.BankAccountRepository;
import org.miki.rabobankdemo.repositories.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Use real database (H2 in-memory by default)
class BankUserServiceImplTest {


    @Autowired
    private BankUserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankUserService userService;

    @Test
    @Transactional
    public void testCreateUser_Success() {
        // Given
        CreateBankUserDTO createBankUserDTO = new CreateBankUserDTO(
                "testLogin",
                "FirstName",
                "LastName",
                "test@example.com",
                "password",
                "USD",
                new BigDecimal("1000.00")
        );

        // When
        BankUser result = userService.create(createBankUserDTO);

        // Then
        assertNotNull(result.getId());
        assertEquals(createBankUserDTO.userLogin(), result.getUserLogin());
        assertEquals(createBankUserDTO.firstName(), result.getFirstName());
        assertEquals(createBankUserDTO.lastName(), result.getLastName());
        assertEquals(createBankUserDTO.email(), result.getEmail());
        // Add more assertions as needed
        
        // Ensure the user and associated bank account are saved in the database
        assertNotNull(result.getId());
        assertNotNull(result.getBankAccounts());
        assertEquals(1, result.getBankAccounts().size());
        assertNotNull(result.getBankAccounts().get(0).getId());

        // You can also assert against the database directly if needed
        BankUser savedUser = userRepository.findByUserLogin(createBankUserDTO.userLogin())
                                            .orElseThrow(() -> new RuntimeException("User not found"));
        assertEquals(createBankUserDTO.userLogin(), savedUser.getUserLogin());
        // Add more assertions for saved user properties

        // Verify that the bank account is saved
        assertEquals(1, savedUser.getBankAccounts().size());
        assertNotNull(savedUser.getBankAccounts().get(0).getId());
    }


}
