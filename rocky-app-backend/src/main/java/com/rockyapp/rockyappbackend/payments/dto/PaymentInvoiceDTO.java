package com.rockyapp.rockyappbackend.payments.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.payments.entity.Payment;
import com.rockyapp.rockyappbackend.utils.enums.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A DTO for the {@link Payment} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInvoiceDTO implements SocleDTO {
    private boolean active;
    private Long id;
    private BigDecimal price;
    private PaymentTypeEnum paymentType;
}