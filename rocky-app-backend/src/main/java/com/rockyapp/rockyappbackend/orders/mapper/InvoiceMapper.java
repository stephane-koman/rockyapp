package com.rockyapp.rockyappbackend.orders.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.customers.mapper.CustomerMapper;
import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import com.rockyapp.rockyappbackend.orders.dto.OrderDTO;
import com.rockyapp.rockyappbackend.orders.entity.Order;
import com.rockyapp.rockyappbackend.payments.mapper.PaymentInvoiceMapper;
import com.rockyapp.rockyappbackend.users.exception.PasswordEmptyException;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InvoiceMapper extends AbstractSocleMapper<Order, OrderDTO> implements SocleMapper<Order, OrderDTO> {

    private InvoiceItemInvoiceMapper invoiceItemInvoiceMapper;
    private CustomerMapper customerMapper;
    private PaymentInvoiceMapper paymentInvoiceMapper;

    @Override
    public Order mapToEntity(OrderDTO model, Order entity) throws NotFoundException, PasswordNotMatchException, PasswordEmptyException {
        return null;
    }

    @Override
    public OrderDTO mapFromEntity(Order entity) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(entity, orderDTO, "active", "customer", "invoiceItems", "payments");

        if(!entity.getOrderItems().isEmpty())
            orderDTO.getInvoiceItems().addAll(entity.getOrderItems().stream().map(invoiceItem -> invoiceItemInvoiceMapper.mapFromEntity(invoiceItem)).collect(Collectors.toList()));

        if(!entity.getPayments().isEmpty())
            orderDTO.getPayments().addAll(entity.getPayments().stream().map(payment -> paymentInvoiceMapper.mapFromEntity(payment)).collect(Collectors.toList()));

        orderDTO.setCustomer(customerMapper.mapFromEntity(entity.getCustomer()));
        orderDTO.setActive(entity.getActive() == 1);

        return orderDTO;
    }
}
