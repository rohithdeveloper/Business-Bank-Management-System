package com.businessbank.serviceimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.businessbank.dto.BankDetailsDto;
import com.businessbank.exception.BankIdNotFoundException;
import com.businessbank.model.BankDetails;
import com.businessbank.repo.BankRepo;
import com.businessbank.service.BankService;

@Service
public class BankServiceImp implements BankService {

	@Autowired
	private BankRepo repo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public BankDetailsDto save(BankDetailsDto detailsDto) {
		BankDetails details = modelMapper.map(detailsDto, BankDetails.class);
		BankDetails savedDetails = repo.save(details);
		BankDetailsDto bankDto = modelMapper.map(savedDetails, BankDetailsDto.class);
		return bankDto;
	}

	@Override
	public BankDetailsDto getBankDetails(String email, String password) {
		BankDetails details = repo.findByEmail(email);

		if (details != null && details.getPassword().equals(password)) {
			return modelMapper.map(details, BankDetailsDto.class);
		} else {
			throw new BankIdNotFoundException("Invalid credentials");

		}
	}

	@Override
	public BankDetailsDto getBankBalance(int id) {
		BankDetails details = repo.findById(id).orElse(null);

		if (details != null) {
			return modelMapper.map(details, BankDetailsDto.class);
		} else {
			throw new BankIdNotFoundException("Bank with Id" + id + " Not Found");
		}
	}

	@Override
	public BankDetailsDto getRecord(int id) {
		BankDetails details = repo.findById(id).orElse(null);

		if (details != null) {
			return modelMapper.map(details, BankDetailsDto.class);
		} else {
			throw new BankIdNotFoundException("Record Not Found");

		}
	}

	@Override
	public BankDetailsDto withdrawAmount(int id, double amount, String password) {
		BankDetails details = repo.findById(id).orElse(null);

		if (details != null && details.getPassword().equals(password) && details.getAmount() > 0) {
			double remainingAmount = details.getAmount() - amount;
			details.setAmount(remainingAmount);
			BankDetails updated = repo.save(details);
			return modelMapper.map(updated, BankDetailsDto.class);
		} else {
			throw new BankIdNotFoundException("Insufficient funds");
		}
	}

	@Override
	public BankDetailsDto DepositeAmount(int id, double amount, String password) {
		BankDetails details = repo.findById(id).orElse(null);

		if (details != null && details.getPassword().equals(password)) {
			double remainingAmount = details.getAmount() + amount;
			details.setAmount(remainingAmount);
			BankDetails updated = repo.save(details);
			return modelMapper.map(updated, BankDetailsDto.class);
		} else {
			throw new BankIdNotFoundException("Insufficient funds");
		}
	}

	// Updated transferAmount method
	@Override
	public BankDetailsDto transferAmount(int id, int tid, double amount, String password) {
		BankDetails sourceAccount = repo.findById(id).orElse(null);
		BankDetails targetAccount = repo.findById(tid).orElse(null);

		if (sourceAccount != null && targetAccount != null && sourceAccount.getAmount() >= amount
				&& sourceAccount.getPassword().equals(password)) {
			double sourceAccountBalance = sourceAccount.getAmount();
			double targetAccountBalance = targetAccount.getAmount();

			// Withdraw from the source account
			double remainingSourceBalance = sourceAccountBalance - amount;
			sourceAccount.setAmount(remainingSourceBalance);
			repo.save(sourceAccount);

			// Deposit into the target account
			double updatedTargetBalance = targetAccountBalance + amount;
			targetAccount.setAmount(updatedTargetBalance);
			repo.save(targetAccount);

			// Mapping BankDetails entity to BankDetailsDto using ModelMapper
			BankDetailsDto transferredAccountDto = modelMapper.map(targetAccount, BankDetailsDto.class);

			return transferredAccountDto;
		} else {
			throw new BankIdNotFoundException("Transfer failed. Please check your details and try again.");
		}
	}

	@Override
	public String closeAccount(int id, String password) {
		Optional<BankDetails> optionalDetails = repo.findById(id);

		if (optionalDetails.isPresent()) {
			BankDetails details = optionalDetails.get();
			if (details.getPassword().equals(password)) {
//	            details.setAmount(0);
//	            repo.save(details);
				repo.delete(details);
				return "Account Closed Successfully";
			} else {
				return "Incorrect Password. Please Check Your Credentials";
			}
		} else {
			throw new BankIdNotFoundException("Account Not Found");
		}
	}

}