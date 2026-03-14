package com.project.data.gym;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BusinessVerifier implements com.project.domain.gym.BusinessVerifier {
    @Override
    public void verifyBusiness(String businessRegistrationNumber, String representativeName, LocalDate businessStartDate) {

    }

    @Override
    public void verifyBusinessRepresentative(String accountHolderName, String bankName, String accountNumber) {

    }
}
