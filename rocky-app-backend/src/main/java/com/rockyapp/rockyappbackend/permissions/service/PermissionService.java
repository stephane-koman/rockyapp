package com.rockyapp.rockyappbackend.permissions.service;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionAlreadyExistsException;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    ResultPagine<SimplePermissionDTO> search(final DefaultCriteriaDTO criteriaDTO, final Pageable pageable);
    Permission findByName(final String name) throws PermissionNotFoundException;
    PermissionDTO findById(final Long id) throws PermissionNotFoundException;
    void create(PermissionDTO permissionDTO) throws PermissionAlreadyExistsException;
    void update(Long permissionId, PermissionDTO permissionDTO) throws PermissionAlreadyExistsException, PermissionNotFoundException;
    void delete(Long permissionId) throws PermissionNotFoundException;

    void changePermissionStatus(Long id, boolean active) throws PermissionNotFoundException;
}
