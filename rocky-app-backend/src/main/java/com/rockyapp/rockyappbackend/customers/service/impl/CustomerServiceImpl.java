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
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;
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
    public ResultPagine<CustomerDTO> searchCustomers(CustomerSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Customer> customerPage = customerDAO.searchCustomers(criteriaDTO, pageable);
        return customerMapper.mapFromEntity(customerPage);
    }

    @Override
    public CustomerDTO findCustomerById(String id) throws CustomerNotFoundException {
        Customer customer = customerDAO.findCustomerByIdAndIsNotDelete(id);
        if(customer == null) throw new CustomerNotFoundException();
        return customerMapper.mapFromEntity(customer);
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) throws CustomerAlreadyExistsException {
        Customer customerExists = customerDAO.findCustomerByNameAndIsNotDelete(customerDTO.getName());
        if(customerExists != null) throw new CustomerAlreadyExistsException(customerDTO.getName());

        Customer customer = new Customer();
        customer = customerMapper.mapToEntity(customerDTO, customer);
        customerDAO.save(customer);
        return customerMapper.mapFromEntity(customer);
    }

    @Override
    public CustomerDTO update(String customerId, CustomerDTO customerDTO) throws CustomerAlreadyExistsException, CustomerNotFoundException {
        Customer customerExists = customerDAO.findCustomerByNameAndIsNotDelete(customerDTO.getName());
        if(!customerExists.getId().equals(customerId)) throw new CustomerAlreadyExistsException(customerDTO.getName());

        Customer customer = customerDAO.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        customer = customerMapper.mapToEntity(customerDTO, customer);
        customer.setUpdatedAt(LocalDateTime.now());
        customer = customerDAO.save(customer);
        return customerMapper.mapFromEntity(customer);
    }

    @Override
    public void delete(String customerId) throws CustomerNotFoundException {
        Customer customer = customerDAO.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        customer.setDelete(1);
        customer.setActive(0);
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setDeletedAt(LocalDateTime.now());
        customerDAO.save(customer);
    }

    @Override
    public void changeCustomerStatus(String id, boolean active) throws CustomerNotFoundException {
        Customer customer = customerDAO.findById(id).orElseThrow(CustomerNotFoundException::new);
        customer.setActive(Boolean.TRUE.equals(active) ? 1 : 0);
        customer.setUpdatedAt(LocalDateTime.now());
        customerDAO.save(customer);
    }
}
