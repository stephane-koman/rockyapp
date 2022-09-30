package com.rockyapp.rockyappbackend.roles.service;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleAlreadyExistsException;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    ResultPagine<SimpleRoleDTO> search(final DefaultCriteriaDTO criteriaDTO, final Pageable pageable);
    Role findByName(final String name) throws RoleNotFoundException;
    RoleDTO findById(final Long id) throws RoleNotFoundException;
    void create(RoleDTO role) throws RoleAlreadyExistsException;
    void update(Long roleId, RoleDTO role) throws RoleAlreadyExistsException, RoleNotFoundException;
    void delete(Long roleId) throws RoleNotFoundException;

    void changeStatus(Long id, boolean active) throws RoleNotFoundException;
}
