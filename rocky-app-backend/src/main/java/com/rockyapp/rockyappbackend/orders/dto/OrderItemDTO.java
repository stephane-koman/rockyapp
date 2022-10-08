package com.rockyapp.rockyappbackend.orders.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.orders.entity.OrderItem;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
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
public class OrderItemDTO implements SocleDTO {
    private Long quantity;
    private BigDecimal price;
    private int active;
    private SimpleProductDTO product;
}