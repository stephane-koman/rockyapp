package com.rockyapp.rockyappbackend.roles.dao;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleDAOCustom {
    Page<Role> searchRoles(DefaultCriteriaDTO criteriaDTO, Pageable pageable);
}
