package com.project.gym.adapter;

import com.project.gym.port.BusinessVerifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BusinessVerifierAdapter implements BusinessVerifier {
    @Override
    public void verifyBusiness(String businessRegistrationNumber, String representativeName, LocalDate businessStartDate) {

    }

    @Override
    public void verifyBusinessAccount(String representativeName, String accountHolderName, String bankName, String accountNumber) {

    }
}
