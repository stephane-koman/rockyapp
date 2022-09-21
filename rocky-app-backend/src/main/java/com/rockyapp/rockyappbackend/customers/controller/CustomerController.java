package com.rockyapp.rockyappbackend.customers.controller;

import com.rockyapp.rockyappbackend.common.dto.StatusDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.dto.CustomerSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.customers.exception.CustomerAlreadyExistsException;
import com.rockyapp.rockyappbackend.customers.exception.CustomerNotFoundException;
import com.rockyapp.rockyappbackend.customers.service.CustomerService;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private CustomerService customerService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_CUSTOMER', 'CREATE_CUSTOMER', 'UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public ResultPagine<CustomerDTO> searchCustomers(@RequestBody(required = false) CustomerSearchCriteriaDTO criteriaDTO,
                                                     Pageable pageable){
        return customerService.searchCustomers(criteriaDTO, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_CUSTOMER', 'CREATE_CUSTOMER', 'UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public CustomerDTO findCustomerById(@PathVariable(name = "id") String id) throws CustomerNotFoundException {
        return customerService.findCustomerById(id);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerAlreadyExistsException {
        return customerService.create(customerDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") String id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException, CustomerAlreadyExistsException {
        return customerService.update(id, customerDTO);
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public void updateUserStatus(@PathVariable(name = "id") String id, @RequestBody StatusDTO statusDTO) throws CustomerNotFoundException {
        customerService.changeCustomerStatus(id, statusDTO.isActive());
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_CUSTOMER')")
    public void deleteCustomer(@PathVariable(name = "id") String id) throws CustomerNotFoundException {
        customerService.delete(id);
    }
}
