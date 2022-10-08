package com.rockyapp.rockyappbackend.orders.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.orders.dto.OrderCreaDTO;
import com.rockyapp.rockyappbackend.orders.dto.OrderDTO;
import com.rockyapp.rockyappbackend.orders.dto.OrderSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.orders.exception.InvoiceNotFoundException;
import org.springframework.data.domain.Pageable;

public interface InvoiceService {
    ResultPagine<OrderDTO> search(final OrderSearchCriteriaDTO criteriaDTO, final Pageable pageable);
    OrderDTO findById(final String id) throws InvoiceNotFoundException;
    void create(OrderCreaDTO invoiceDTO);
    void update(String invoiceId, OrderCreaDTO invoiceDTO) throws InvoiceNotFoundException;
    void delete(String invoiceId) throws InvoiceNotFoundException;
    void changeStatus(String id, boolean active) throws InvoiceNotFoundException;
}
