package com.rockyapp.rockyappbackend.customers.controller;

import com.rockyapp.rockyappbackend.common.dto.StatusDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.dto.CustomerSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.customers.exception.CustomerAlreadyExistsException;
import com.rockyapp.rockyappbackend.customers.exception.CustomerNotFoundException;
import com.rockyapp.rockyappbackend.customers.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private CustomerService customerService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_CUSTOMER', 'CREATE_CUSTOMER', 'UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public ResponseEntity<ResultPagine<CustomerDTO>> search(@RequestBody(required = false) CustomerSearchCriteriaDTO criteriaDTO,
                                 Pageable pageable){
        ResultPagine<CustomerDTO> resultPagine = customerService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(resultPagine);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_CUSTOMER', 'CREATE_CUSTOMER', 'UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public ResponseEntity<CustomerDTO> findById(@PathVariable(name = "id") String id) throws CustomerNotFoundException {
        CustomerDTO customerDTO = customerService.findById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public ResponseEntity<Void> create(@RequestBody CustomerDTO customerDTO) throws CustomerAlreadyExistsException {
        customerService.create(customerDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public ResponseEntity<Void> update(@PathVariable(name = "id") String id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException, CustomerAlreadyExistsException {
        customerService.update(id, customerDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_CUSTOMER', 'DELETE_CUSTOMER')")
    public ResponseEntity<Void> updateStatus(@PathVariable(name = "id") String id, @RequestBody StatusDTO statusDTO) throws CustomerNotFoundException {
        customerService.changeStatus(id, statusDTO.isActive());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_CUSTOMER')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) throws CustomerNotFoundException {
        customerService.delete(id);
        return ResponseEntity.ok().build();
    }
}
