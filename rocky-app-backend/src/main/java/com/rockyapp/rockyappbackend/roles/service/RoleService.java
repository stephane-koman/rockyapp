package com.rockyapp.rockyappbackend.roles.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    ResultPagine<RoleDTO> searchRoleByNameAndIsNotDelete(final String name, final int active, final Pageable pageable);
    Role findRoleByName(final String name) throws RoleNotFoundException;
}
