package com.rockyapp.rockyappbackend.customers.dao;

import com.rockyapp.rockyappbackend.customers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface CustomerDAO extends PagingAndSortingRepository<Customer, String>, CustomerDAOCustom {
    @Query("SELECT c FROM Customer c " +
            "WHERE (LOWER(c.name) like LOWER(CONCAT('%',:name,'%'))) " +
            "AND (c.active = :active or :active = 2) AND (c.delete = 0) " +
            "ORDER BY c.name"
    )
    Page<Customer> searchCustomerByNameAndDeleteIsNot(@Param("name") String name, @Param("active") int active, Pageable pageable);

    @Query("SELECT c FROM Customer c " +
            "WHERE (LOWER(c.name) = LOWER(:name)) AND c.delete = 0")
    Customer findCustomerByNameAndIsNotDelete(@Param("name") String name);

    @Query("SELECT c FROM Customer c " +
            "WHERE (LOWER(c.name) = LOWER(:name)) " +
            "AND c.active = 1 AND c.delete = 0")
    Customer findCustomerByNameAndIsActiveAndIsNotDelete(@Param("name") String name);

    @Query("SELECT c FROM Customer c " +
            "WHERE c.id = :id " +
            "AND c.active = 1 AND c.delete = 0")
    Customer findCustomerByIdAndIsActiveAndIsNotDelete(@Param("id") String id);

    @Query("SELECT c FROM Customer c " +
            "WHERE c.id = :id AND c.delete = 0")
    Customer findCustomerByIdAndIsNotDelete(@Param("id") String id);

    boolean existsByName(String name);
}
