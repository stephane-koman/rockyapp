package com.rockyapp.rockyappbackend.customers.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.customers.dao.CustomerDAO;
import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.dto.CustomerSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.customers.entity.Customer;
import com.rockyapp.rockyappbackend.customers.exception.CustomerAlreadyExistsException;
import com.rockyapp.rockyappbackend.customers.exception.CustomerNotFoundException;
import com.rockyapp.rockyappbackend.customers.mapper.CustomerMapper;
import com.rockyapp.rockyappbackend.customers.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;
    private CustomerMapper customerMapper;

    @Override
    public ResultPagine<CustomerDTO> search(CustomerSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Customer> customerPage = customerDAO.search(criteriaDTO, pageable);
        return customerMapper.mapFromEntity(customerPage);
    }

    public Customer findCustomerById(String id) throws CustomerNotFoundException {
        Customer customer = customerDAO.findCustomerByIdAndIsNotDelete(id);
        if(customer == null) throw new CustomerNotFoundException();
        return customer;
    }
    @Override
    public CustomerDTO findById(String id) throws CustomerNotFoundException {
        Customer customer = this.findCustomerById(id);
        return customerMapper.mapFromEntity(customer);
    }

    @Override
    public void create(CustomerDTO customerDTO) throws CustomerAlreadyExistsException {
        Customer customerExists = customerDAO.findCustomerByNameAndIsNotDelete(customerDTO.getName());
        if(customerExists != null) throw new CustomerAlreadyExistsException(customerDTO.getName());

        Customer customer = new Customer();
        customer = customerMapper.mapToEntity(customerDTO, customer);
        customerDAO.save(customer);
    }

    @Override
    public void update(String customerId, CustomerDTO customerDTO) throws CustomerAlreadyExistsException, CustomerNotFoundException {
        Customer customerExists = customerDAO.findCustomerByNameAndIsNotDelete(customerDTO.getName());
        if(customerExists != null && !customerExists.getId().equals(customerId)) throw new CustomerAlreadyExistsException(customerDTO.getName());

        Customer customer = this.findCustomerById(customerId);
        customer = customerMapper.mapToEntity(customerDTO, customer);
        customer.setUpdatedAt(LocalDateTime.now());
        customerDAO.save(customer);
    }

    @Override
    public void delete(String customerId) throws CustomerNotFoundException {
        Customer customer = this.findCustomerById(customerId);
        customer.setDelete(1);
        customer.setActive(0);
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setDeletedAt(LocalDateTime.now());
        customerDAO.save(customer);
    }

    @Override
    public void changeStatus(String id, boolean active) throws CustomerNotFoundException {
        Customer customer = this.findCustomerById(id);
        customer.setActive(Boolean.TRUE.equals(active) ? 1 : 0);
        customer.setUpdatedAt(LocalDateTime.now());
        customerDAO.save(customer);
    }
}
