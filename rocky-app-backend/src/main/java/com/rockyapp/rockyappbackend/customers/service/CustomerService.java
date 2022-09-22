package com.rockyapp.rockyappbackend.customers.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.dto.CustomerSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.customers.exception.CustomerAlreadyExistsException;
import com.rockyapp.rockyappbackend.customers.exception.CustomerNotFoundException;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    ResultPagine<CustomerDTO> searchCustomers(final CustomerSearchCriteriaDTO criteriaDTO, final Pageable pageable);
    CustomerDTO findCustomerById(final String id) throws CustomerNotFoundException;
    CustomerDTO create(CustomerDTO customer) throws CustomerAlreadyExistsException;
    CustomerDTO update(String customerId, CustomerDTO customer) throws CustomerAlreadyExistsException, CustomerNotFoundException;
    void delete(String customerId) throws CustomerNotFoundException;

    void changeCustomerStatus(String id, boolean active) throws CustomerNotFoundException;
}
