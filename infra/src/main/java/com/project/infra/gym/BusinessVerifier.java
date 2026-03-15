package com.project.infra.gym;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BusinessVerifier implements com.project.domain.gym.BusinessVerifier {
    @Override
    public void verifyBusiness(String businessRegistrationNumber, String representativeName, LocalDate businessStartDate) {

    }

    @Override
    public void verifyBusinessAccount(String representativeName, String accountHolderName, String bankName, String accountNumber) {

    }
}
