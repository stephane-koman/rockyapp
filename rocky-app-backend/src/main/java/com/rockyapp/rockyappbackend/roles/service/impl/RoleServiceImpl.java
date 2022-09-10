package com.rockyapp.rockyappbackend.roles.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.mapper.RoleMapper;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleDAO roleDAO;
    private RoleMapper roleMapper;

    @Override
    public ResultPagine<RoleDTO> searchRoleByNameAndIsNotDelete(String name, int active, Pageable pageable) {
        Page<Role> rolePage = this.roleDAO.searchRoleByNameAndDeleteIsNot(name, active, pageable);

        return this.roleMapper.mapFromEntity(rolePage);
    }

    @Override
    public Role findRoleByName(String name) throws RoleNotFoundException {
        Role role = roleDAO.findRoleByNameAndIsActiveAndIsNotDelete(name);

        if(role == null) throw new RoleNotFoundException();

        return role;
    }
}
