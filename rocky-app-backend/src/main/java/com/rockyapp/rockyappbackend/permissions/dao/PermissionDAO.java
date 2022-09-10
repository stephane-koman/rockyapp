package com.rockyapp.rockyappbackend.permissions.dao;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PermissionDAO extends PagingAndSortingRepository<Permission, Long> {

    @Query("SELECT p FROM Permission p " +
            "WHERE (LOWER(p.name) = LOWER(:name)) " +
            "AND p.active = 1 AND p.delete = 0")
    Permission findPermissionByNameAndIsActiveAndIsNotDelete(String name);
}
