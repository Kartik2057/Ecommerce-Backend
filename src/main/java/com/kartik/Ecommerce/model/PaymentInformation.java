package com.kartik.Ecommerce.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Embeddable
@Data
public class PaymentInformation {
    private String cardholderName;
    private String cardNumber;
    private LocalDate expirationDate;
    private String cvv;
}
