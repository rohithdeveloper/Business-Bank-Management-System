package com.businessbank.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.businessbank.dto.BankDetailsDto;
import com.businessbank.service.BankService;

@RestController
@RequestMapping("/api")
public class BusinessBankController {

	@Autowired
	private BankService bankService;

	// save details

	@PostMapping("/save")
	public String saveDetails(@RequestBody BankDetailsDto detailsDto) {
		BankDetailsDto savedDetails = bankService.save(detailsDto);

		if (savedDetails != null) {
			return "registration success";
		} else {
			return "registrationfailed";
		}
	}

	// login details

	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> requestBody, ModelMap map) {
		String email = requestBody.get("email");
		String password = requestBody.get("password");

		BankDetailsDto details = bankService.getBankDetails(email, password);

		if (details != null) {
			map.put("detail", details);
			return "login status success";
		} else {
			return "login status failed";
		}
	}

	// check balance

	@GetMapping("/checkbalance/{id}")
	public String getBalance(@PathVariable int id, ModelMap map) {
		BankDetailsDto details = bankService.getBankBalance(id);

		if (details != null) {
			map.put("detail", details);
			return "Balance:" + details.getAmount();
		} else {
			return "notfound";
		}
	}

	// withdraw

	@PostMapping("/withdraw/{id}")
	public String withdrawAmount(@PathVariable int id, @RequestBody Map<String, Object> request, ModelMap map) {
		double amount = Double.parseDouble(request.get("amount").toString());
		String password = request.get("password").toString();

		BankDetailsDto withdraw = bankService.withdrawAmount(id, amount, password);

		if (withdraw != null) {
			map.put("balance", withdraw.getAmount());
			return "Withdrawn amount: " + amount + ". Remaining balance: " + withdraw.getAmount();
		} else {
			throw new RuntimeException("Withdrawal operation failed");
		}
	}

	// deposit

	@PostMapping("/depositeamount/{id}")
	public String depositeAmount(@PathVariable int id, @RequestBody Map<String, Object> request, ModelMap map) {
		double amount = Double.parseDouble(request.get("amount").toString());
		String password = (String) request.get("password");

		BankDetailsDto deposit = bankService.DepositeAmount(id, amount, password);

		if (deposit != null) {
			map.put("balance", deposit);
			return "Deposited Amount: " + amount + ". Total Amount: " + deposit.getAmount();
		} else {
			throw new RuntimeException("Deposit operation failed");
		}
	}

	// transfer amount

	@PostMapping("/transferamount/{id}")
	public String transferAmount(@PathVariable int id, @RequestBody Map<String, Object> request) {
		int targetAccountId = Integer.parseInt(request.get("tid").toString());
		double amount = Double.parseDouble(request.get("amount").toString());
		String password = request.get("password").toString();

		BankDetailsDto transferredAccount = bankService.transferAmount(id, targetAccountId, amount, password);

		if (transferredAccount != null) {
			return "Transfer successful. Amount transferred: " + amount;

		} else {
			return "Transfer failed. Please check your details and try again.";
		}
	}

	// check record is present or not

	@GetMapping("/record/{id}")
	public BankDetailsDto getRecord(@PathVariable int id) {
		BankDetailsDto details = bankService.getRecord(id);

		if (details != null) {
			return details;
		} else {
			throw new RuntimeException("Record not found");
		}
	}

	// check record is present or not

	@GetMapping("/closeaccountstatus/{id}")
	public String getCloseAccountStatus(@PathVariable int id, @RequestParam String password) {
		String status = bankService.closeAccount(id, password);

		if ("Account Closed Successfully".equals(status)) {
			return "Account closed successfully for ID: " + id;
		} else {
			throw new RuntimeException("Record not found");
		}
	}

	// close account

	@PostMapping("/closeaccount/{id}")
	public Map<String, String> closeAccount(@PathVariable int id, @RequestParam String password) {
		Map<String, String> response = new HashMap<>();
		String status = bankService.closeAccount(id, password);
		response.put("status", status);
		return response;
	}

}
