package com.rockyapp.rockyappbackend.roles.dao;

import com.rockyapp.rockyappbackend.roles.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface RoleDAO extends PagingAndSortingRepository<Role, Long>, RoleDAOCustom {

    @Query("SELECT r FROM Role r " +
            "WHERE (LOWER(r.name) = LOWER(:name)) AND r.delete = 0")
    Role findRoleByNameAndIsNotDelete(@Param("name") String name);

    @Query("SELECT r FROM Role r " +
            "WHERE r.id = :id AND r.delete = 0")
    Role findRoleByIdAndIsNotDelete(@Param("id") Long id);
}
