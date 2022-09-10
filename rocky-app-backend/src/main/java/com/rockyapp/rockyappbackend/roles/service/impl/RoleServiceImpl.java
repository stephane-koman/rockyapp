package com.rockyapp.rockyappbackend.roles.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleAlreadyExistsException;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.mapper.RoleMapper;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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

    @Override
    public RoleDTO findRoleById(Long id) throws RoleNotFoundException {
        Role role = roleDAO.findRoleByIdAndIsActiveAndIsNotDelete(id);

        if(role == null) throw new RoleNotFoundException();

        return roleMapper.mapFromEntity(role);
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) throws RoleAlreadyExistsException {
        boolean roleExists = roleDAO.existsByName(roleDTO.getName());

        if(Boolean.TRUE.equals(roleExists)) throw new RoleAlreadyExistsException(roleDTO.getName());

        Role role = new Role();
        role = roleMapper.mapToEntity(roleDTO, role);
        role = roleDAO.save(role);

        return roleMapper.mapFromEntity(role);
    }

    @Override
    public RoleDTO update(Long roleId, RoleDTO roleDTO) throws RoleAlreadyExistsException, RoleNotFoundException {
        Role role  = this.findRoleByName(roleDTO.getName());
        if(!role.getId().equals(roleId)) throw new RoleAlreadyExistsException(roleDTO.getName());

        role = roleMapper.mapToEntity(roleDTO, role);
        role.setUpdatedAt(LocalDateTime.now());
        role = roleDAO.save(role);

        return roleMapper.mapFromEntity(role);
    }

    @Override
    public void delete(Long roleId) throws RoleNotFoundException {
        Role role = roleDAO.findById(roleId).orElseThrow(RoleNotFoundException::new);
        role.setDelete(1);
        role.setActive(0);
        role.setUpdatedAt(LocalDateTime.now());
        role.setDeletedAt(LocalDateTime.now());
        roleDAO.save(role);
    }
}
