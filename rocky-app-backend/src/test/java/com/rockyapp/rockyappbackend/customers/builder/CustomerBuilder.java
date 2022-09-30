package com.rockyapp.rockyappbackend.customers.builder;

import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.entity.Customer;

public class CustomerBuilder {

    public static CustomerDTO getDto() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId("1");
        dto.setName("ADMIN");
        return dto;
    }

    public static Customer getEntity() {
        Customer entity = new Customer();
        entity.setId("1");
        entity.setName("ADMIN");
        return entity;
    }
}