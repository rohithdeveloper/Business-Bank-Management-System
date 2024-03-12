package com.businessbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsDto {

    private int bank_id;
    private String name;
    private String password;
    private String email;
    private long mobile;
    private String pan;
    private String address;
    private double amount;

}