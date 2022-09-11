package com.rockyapp.rockyappbackend.roles.dao;

import com.rockyapp.rockyappbackend.roles.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface RoleDAO extends PagingAndSortingRepository<Role, Long>, RoleDAOCustom {
    @Query("SELECT r FROM Role r " +
            "WHERE (LOWER(r.name) like LOWER(CONCAT('%',:name,'%'))) " +
            "AND (r.active = :active or :active = 2) AND (r.delete = 0) " +
            "ORDER BY r.name"
    )
    Page<Role> searchRoleByNameAndDeleteIsNot(@Param("name") String name, @Param("active") int active, Pageable pageable);

    @Query("SELECT r FROM Role r " +
            "WHERE (LOWER(r.name) = LOWER(:name)) AND r.delete = 0")
    Role findRoleByNameAndIsNotDelete(@Param("name") String name);

    @Query("SELECT r FROM Role r " +
            "WHERE (LOWER(r.name) = LOWER(:name)) " +
            "AND r.active = 1 AND r.delete = 0")
    Role findRoleByNameAndIsActiveAndIsNotDelete(@Param("name") String name);

    @Query("SELECT r FROM Role r " +
            "WHERE r.id = :id " +
            "AND r.active = 1 AND r.delete = 0")
    Role findRoleByIdAndIsActiveAndIsNotDelete(@Param("id") Long id);

    @Query("SELECT r FROM Role r " +
            "WHERE r.id = :id AND r.delete = 0")
    Role findRoleByIdAndIsNotDelete(@Param("id") Long id);

    boolean existsByName(String name);
}
