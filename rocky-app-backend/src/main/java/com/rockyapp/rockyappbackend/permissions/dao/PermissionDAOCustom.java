package com.rockyapp.rockyappbackend.permissions.dao;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionDAOCustom {
    Page<Permission> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable);
}
