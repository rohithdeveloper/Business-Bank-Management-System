package com.businessbank.model;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int bank_id;
	@Column(name = "name")
	private String name;
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	@Column(name = "mobile")
	private long mobile;
	@Column(name = "pan")
	private String pan;
	@Column(name = "address")
	private String address;
	@Column(name = "amount")
	private double amount;

}
