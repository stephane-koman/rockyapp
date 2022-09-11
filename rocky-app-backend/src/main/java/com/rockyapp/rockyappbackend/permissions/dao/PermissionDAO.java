package com.rockyapp.rockyappbackend.permissions.dao;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PermissionDAO extends PagingAndSortingRepository<Permission, Long> {
    @Query("SELECT p FROM Permission p " +
            "WHERE (LOWER(p.name) like LOWER(CONCAT('%',:name,'%'))) " +
            "AND (p.active = :active or :active = 2) AND (p.delete = 0) " +
            "ORDER BY p.name"
    )
    Page<Permission> searchPermissionByNameAndDeleteIsNot(@Param("name") String name, @Param("active") int active, Pageable pageable);

    @Query("SELECT p FROM Permission p " +
            "WHERE (LOWER(p.name) = LOWER(:name)) " +
            "AND p.active = 1 AND p.delete = 0")
    Permission findPermissionByNameAndIsActiveAndIsNotDelete(String name);

    @Query("SELECT p FROM Permission p " +
            "WHERE p.id = :id " +
            "AND p.active = 1 AND p.delete = 0")
    Permission findPermissionByIdAndIsActiveAndIsNotDelete(@Param("id") Long id);

    boolean existsByName(String name);
}
