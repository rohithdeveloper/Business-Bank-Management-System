package com.businessbank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.businessbank.model.BankDetails;

@Repository
public interface BankRepo extends JpaRepository<BankDetails, Integer> {

	BankDetails findByEmail(String email);

}
