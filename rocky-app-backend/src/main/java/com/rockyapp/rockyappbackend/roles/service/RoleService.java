package com.rockyapp.rockyappbackend.roles.service;

import com.rockyapp.rockyappbackend.common.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    ResultPagine<RoleDTO> searchRoleByNameAndIsNotDelete(final String name, final int active, final Pageable pageable);
}
