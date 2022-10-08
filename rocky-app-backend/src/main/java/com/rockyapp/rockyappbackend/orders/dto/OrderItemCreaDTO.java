package com.rockyapp.rockyappbackend.orders.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.orders.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A DTO for the {@link OrderItem} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCreaDTO implements SocleDTO {
    private Long quantity;
    private BigDecimal price;
    private boolean active;
    private String productId;
    private String invoiceId;
}