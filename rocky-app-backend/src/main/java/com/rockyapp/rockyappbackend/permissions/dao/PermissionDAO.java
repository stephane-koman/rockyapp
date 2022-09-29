package com.rockyapp.rockyappbackend.permissions.dao;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PermissionDAO extends PagingAndSortingRepository<Permission, Long>, PermissionDAOCustom {

    @Query("SELECT p FROM Permission p " +
            "WHERE (LOWER(p.name) = LOWER(:name)) AND p.delete = 0")
    Permission findPermissionByNameAndIsNotDelete(String name);

    @Query("SELECT p FROM Permission p " +
            "WHERE p.id = :id AND p.delete = 0")
    Permission findPermissionByIdAndIsNotDelete(@Param("id") Long id);

    boolean existsByName(String name);
}
