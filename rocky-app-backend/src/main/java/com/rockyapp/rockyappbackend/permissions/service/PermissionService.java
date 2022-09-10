package com.rockyapp.rockyappbackend.permissions.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionAlreadyExistsException;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    ResultPagine<SimplePermissionDTO> searchPermissionByNameAndIsNotDelete(final String name, final int active, final Pageable pageable);
    Permission findPermissionByName(final String name) throws PermissionNotFoundException;
    PermissionDTO findPermissionById(final Long id) throws PermissionNotFoundException;
    PermissionDTO create(PermissionDTO permissionDTO) throws PermissionAlreadyExistsException;
    PermissionDTO update(Long permissionId, PermissionDTO permissionDTO) throws PermissionAlreadyExistsException, PermissionNotFoundException;
    void delete(Long permissionId) throws PermissionNotFoundException;
}
