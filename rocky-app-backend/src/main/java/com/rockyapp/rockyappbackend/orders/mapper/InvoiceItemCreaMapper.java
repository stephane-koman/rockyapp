package com.rockyapp.rockyappbackend.orders.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.orders.dto.OrderItemCreaDTO;
import com.rockyapp.rockyappbackend.orders.entity.OrderItem;
import com.rockyapp.rockyappbackend.orders.entity.OrderItemId;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvoiceItemCreaMapper  extends AbstractSocleMapper<OrderItem, OrderItemCreaDTO> implements SocleMapper<OrderItem, OrderItemCreaDTO> {
    @Override
    public OrderItem mapToEntity(OrderItemCreaDTO model, OrderItem entity) {
        BeanUtils.copyProperties(model, entity, "active", "productId", "invoiceId");
        entity.setId(new OrderItemId(model.getInvoiceId(), model.getProductId()));
        return entity;
    }

    @Override
    public OrderItemCreaDTO mapFromEntity(OrderItem entity) {
        return null;
    }
}
