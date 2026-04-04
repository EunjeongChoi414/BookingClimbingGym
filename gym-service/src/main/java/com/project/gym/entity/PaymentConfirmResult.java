package com.project.gym.entity;

public class PaymentConfirmResult {
    private final boolean success;
    private final String receiptUrl;
    private final String errorMessage;

    private PaymentConfirmResult(boolean success, String receiptUrl, String errorMessage) {
        this.success = success;
        this.receiptUrl = receiptUrl;
        this.errorMessage = errorMessage;
    }

    public static PaymentConfirmResult success(String receiptUrl) {
        return new PaymentConfirmResult(true, receiptUrl, null);
    }

    public static PaymentConfirmResult failure(String errorMessage) {
        return new PaymentConfirmResult(false, null, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
