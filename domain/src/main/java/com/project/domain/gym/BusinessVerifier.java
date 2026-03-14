package com.project.domain.gym;

import java.time.LocalDate;

public interface BusinessVerifier {
    void verifyBusiness(String businessRegistrationNumber, String representativeName, LocalDate businessStartDate);
    void verifyBusinessRepresentative(String accountHolderName, String bankName, String accountNumber);
}
