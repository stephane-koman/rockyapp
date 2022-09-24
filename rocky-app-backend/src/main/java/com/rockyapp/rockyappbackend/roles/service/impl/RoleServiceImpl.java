package com.rockyapp.rockyappbackend.roles.service.impl;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleAlreadyExistsException;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.mapper.RoleMapper;
import com.rockyapp.rockyappbackend.roles.mapper.SimpleRoleMapper;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;
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
    private SimpleRoleMapper simpleRoleMapper;

    @Override
    public ResultPagine<SimpleRoleDTO> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Role> rolePage = this.roleDAO.searchRoles(criteriaDTO, pageable);

        return this.simpleRoleMapper.mapFromEntity(rolePage);
    }

    @Override
    public Role findByName(String name) throws RoleNotFoundException {
        Role role = roleDAO.findRoleByNameAndIsNotDelete(name);

        if(role == null) throw new RoleNotFoundException();

        return role;
    }

    @Override
    public RoleDTO findById(Long id) throws RoleNotFoundException {
        Role role = roleDAO.findRoleByIdAndIsNotDelete(id);

        if(role == null) throw new RoleNotFoundException();

        return roleMapper.mapFromEntity(role);
    }

    @Override
<<<<<<< refs/remotes/origin/main
    public void create(RoleDTO roleDTO) throws RoleAlreadyExistsException {
        boolean roleExists = roleDAO.existsByName(roleDTO.getName());
=======
    public RoleDTO create(RoleDTO roleDTO) throws RoleAlreadyExistsException {
        Role roleExists = roleDAO.findRoleByNameAndIsNotDelete(roleDTO.getName());
>>>>>>> Adding customer API

        if(roleExists != null) throw new RoleAlreadyExistsException(roleDTO.getName());

        Role role = new Role();
        role = roleMapper.mapToEntity(roleDTO, role);
        roleDAO.save(role);
    }

    @Override
    public void update(Long roleId, RoleDTO roleDTO) throws RoleAlreadyExistsException, RoleNotFoundException {
        Role role  = this.findByName(roleDTO.getName());

        if(role != null && !role.getId().equals(roleId)) throw new RoleAlreadyExistsException(roleDTO.getName());

        role = roleDAO.findById(roleId).orElseThrow(RoleNotFoundException::new);
        role = roleMapper.mapToEntity(roleDTO, role);
        role.setUpdatedAt(LocalDateTime.now());
        roleDAO.save(role);
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

    @Override
    public void changeRoleStatus(Long id, boolean active) throws RoleNotFoundException {
        Role role = roleDAO.findById(id).orElseThrow(RoleNotFoundException::new);
        role.setActive(Boolean.TRUE.equals(active) ? 1 : 0);
        role.setUpdatedAt(LocalDateTime.now());
        roleDAO.save(role);
    }
}
