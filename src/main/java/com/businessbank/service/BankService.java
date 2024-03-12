package com.businessbank.service;

import com.businessbank.dto.BankDetailsDto;

public interface BankService {

	BankDetailsDto save(BankDetailsDto detailsDto);

	BankDetailsDto getBankDetails(String email, String password);

	BankDetailsDto getBankBalance(int id);

	BankDetailsDto getRecord(int id);

	BankDetailsDto withdrawAmount(int id, double amount, String password);

	BankDetailsDto DepositeAmount(int id, double amount, String password);

	BankDetailsDto transferAmount(int id, int tid, double amount, String password);

	String closeAccount(int id, String password);
}
