package com.rockyapp.rockyappbackend.invoices.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.invoices.entity.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * A DTO for the {@link Invoice} entity
 */
@Data
@AllArgsConstructor
public class InvoiceDTO implements SocleDTO {
    private final int active;
    private final String id;
    private final BigDecimal price;
}