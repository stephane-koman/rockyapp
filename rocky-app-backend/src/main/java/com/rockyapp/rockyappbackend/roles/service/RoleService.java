package com.rockyapp.rockyappbackend.roles.service;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleAlreadyExistsException;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    ResultPagine<SimpleRoleDTO> searchRoles(final DefaultCriteriaDTO criteriaDTO, final Pageable pageable);
    Role findRoleByName(final String name) throws RoleNotFoundException;
    RoleDTO findRoleById(final Long id) throws RoleNotFoundException;
    RoleDTO create(RoleDTO role) throws RoleAlreadyExistsException;
    RoleDTO update(Long roleId, RoleDTO role) throws RoleAlreadyExistsException, RoleNotFoundException;
    void delete(Long roleId) throws RoleNotFoundException;

    void changeRoleStatus(Long id, boolean active) throws RoleNotFoundException;
}
