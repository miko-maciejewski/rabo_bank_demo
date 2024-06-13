package org.miki.rabobankdemo.dto;

/**
 * Data transfer object bank user used  by REST interface
 * 
 * @author Mikołaj Maciejewski
 *
 */
public record BankUserDTO(Long id, String userLogin, String firstName, String lastName, String email) { }
