package com.rockyapp.rockyappbackend.payments.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.invoices.dto.InvoiceDTO;
import com.rockyapp.rockyappbackend.payments.entity.Payment;
import com.rockyapp.rockyappbackend.utils.enums.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A DTO for the {@link Payment} entity
 */
@Data
@AllArgsConstructor
public class PaymentDTO implements SocleDTO {
    private final int active;
    private final Long id;
    private final InvoiceDTO invoiceDTO;
    private final PaymentTypeEnum paymentType;
}