package com.rockyapp.rockyappbackend.orders.dto;

import com.rockyapp.rockyappbackend.orders.entity.Order;
import com.rockyapp.rockyappbackend.utils.enums.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A DTO for the {@link Order} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchCriteriaDTO extends OrderCreaDTO {
    PaymentStatusEnum paymentStatus;
    BigDecimal restPayments;
    BigDecimal totalPayments;
}