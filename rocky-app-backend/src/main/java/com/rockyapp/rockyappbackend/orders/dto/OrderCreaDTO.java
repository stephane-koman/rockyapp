package com.rockyapp.rockyappbackend.orders.dto;

import com.rockyapp.rockyappbackend.orders.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link Order} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreaDTO extends OrderPaymentDTO {
    private String customerId;
    Set<OrderItemCreaDTO> invoiceItems = new HashSet<>();
}