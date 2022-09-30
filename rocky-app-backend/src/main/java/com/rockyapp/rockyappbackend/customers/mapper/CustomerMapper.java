package com.rockyapp.rockyappbackend.customers.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerMapper extends AbstractSocleMapper<Customer
        , CustomerDTO> implements SocleMapper<Customer, CustomerDTO> {

    @Override
    public Customer mapToEntity(CustomerDTO model, Customer entity) {
        BeanUtils.copyProperties(model, entity, "id", "active");
        entity.setActive(model.isActive() ? 1 : 0);

        return entity;
    }

    @Override
    public CustomerDTO mapFromEntity(Customer entity) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(entity, customerDTO, "active");
        customerDTO.setActive(entity.getActive() == 1);

        return customerDTO;
    }
}
