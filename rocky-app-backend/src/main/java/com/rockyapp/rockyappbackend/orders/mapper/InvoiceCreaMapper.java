package com.rockyapp.rockyappbackend.orders.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.customers.dao.CustomerDAO;
import com.rockyapp.rockyappbackend.orders.dto.OrderCreaDTO;
import com.rockyapp.rockyappbackend.orders.entity.Order;
import com.rockyapp.rockyappbackend.orders.entity.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InvoiceCreaMapper extends AbstractSocleMapper<Order, OrderCreaDTO> implements SocleMapper<Order, OrderCreaDTO> {

    private InvoiceItemCreaMapper invoiceItemCreaMapper;
    private CustomerDAO customerDAO;

    @Override
    public Order mapToEntity(OrderCreaDTO model, Order entity) {
        BeanUtils.copyProperties(model, entity, "active", "customerId", "invoiceItems");
        entity.setCustomer(customerDAO.findCustomerByIdAndIsNotDelete(model.getCustomerId()));
        if(!entity.getOrderItems().isEmpty())
            entity.getOrderItems().addAll(model.getInvoiceItems().stream().map(invoiceItemCreaDTO -> invoiceItemCreaMapper.mapToEntity(invoiceItemCreaDTO, new OrderItem())).collect(Collectors.toList()));

        return null;
    }

    @Override
    public OrderCreaDTO mapFromEntity(Order entity) {
        return null;
    }
}
