package com.project.gym.port;

import java.time.LocalDate;

public interface BusinessVerifier {
    void verifyBusiness(String businessRegistrationNumber, String representativeName, LocalDate businessStartDate);

    void verifyBusinessAccount(
            String representativeName, String accountHolderName, String bankName, String accountNumber);
}
