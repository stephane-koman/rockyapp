package com.rockyapp.rockyappbackend.orders.dto;

import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.orders.entity.Order;
import com.rockyapp.rockyappbackend.payments.dto.PaymentInvoiceDTO;
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
public class OrderDTO extends OrderPaymentDTO {
    private CustomerDTO customer;
    Set<OrderItemInvoiceDTO> invoiceItems = new HashSet<>();
    Set<PaymentInvoiceDTO> payments = new HashSet<>();
}