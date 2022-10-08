package com.rockyapp.rockyappbackend.orders.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.orders.dto.OrderItemInvoiceDTO;
import com.rockyapp.rockyappbackend.orders.entity.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvoiceItemInvoiceMapper extends AbstractSocleMapper<OrderItem, OrderItemInvoiceDTO> implements SocleMapper<OrderItem, OrderItemInvoiceDTO> {

    @Override
    public OrderItem mapToEntity(OrderItemInvoiceDTO model, OrderItem entity) {
        return null;
    }

    @Override
    public OrderItemInvoiceDTO mapFromEntity(OrderItem entity) {
        OrderItemInvoiceDTO orderItemInvoiceDTO = new OrderItemInvoiceDTO();
        BeanUtils.copyProperties(entity, orderItemInvoiceDTO, "active", "product", "volume");

        orderItemInvoiceDTO.setProductId(entity.getProduct().getId());
        orderItemInvoiceDTO.setActive(entity.getActive() == 1);

        return orderItemInvoiceDTO;
    }
}
