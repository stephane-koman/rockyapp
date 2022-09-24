package com.rockyapp.rockyappbackend.customers.dao;

import com.rockyapp.rockyappbackend.customers.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface CustomerDAO extends PagingAndSortingRepository<Customer, String>, CustomerDAOCustom {

    @Query("SELECT c FROM Customer c " +
            "WHERE (LOWER(c.name) = LOWER(:name)) AND c.delete = 0")
    Customer findCustomerByNameAndIsNotDelete(@Param("name") String name);

    @Query("SELECT c FROM Customer c " +
            "WHERE c.id = :id AND c.delete = 0")
    Customer findCustomerByIdAndIsNotDelete(@Param("id") String id);
}
