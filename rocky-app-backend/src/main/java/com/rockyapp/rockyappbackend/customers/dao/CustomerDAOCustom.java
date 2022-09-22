package com.rockyapp.rockyappbackend.customers.dao;

import com.rockyapp.rockyappbackend.customers.dto.CustomerSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.customers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerDAOCustom {
    Page<Customer> searchCustomers(CustomerSearchCriteriaDTO criteriaDTO, Pageable pageable);
}
