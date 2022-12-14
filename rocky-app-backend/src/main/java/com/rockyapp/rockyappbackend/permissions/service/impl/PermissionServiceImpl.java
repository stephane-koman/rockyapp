package com.rockyapp.rockyappbackend.permissions.service.impl;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dao.PermissionDAO;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionAlreadyExistsException;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.mapper.PermissionMapper;
import com.rockyapp.rockyappbackend.permissions.mapper.SimplePermissionMapper;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private PermissionDAO permissionDAO;
    private PermissionMapper  permissionMapper;
    private SimplePermissionMapper simplePermissionMapper;

    @Override
    public ResultPagine<SimplePermissionDTO> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Permission> permissionPage = permissionDAO.search(criteriaDTO, pageable);
        return simplePermissionMapper.mapFromEntity(permissionPage);
    }

    @Override
    public Permission findByName(String name) throws PermissionNotFoundException {
        Permission permission = permissionDAO.findPermissionByNameAndIsNotDelete(name);

        if(permission == null) throw new PermissionNotFoundException();

        return permission;
    }

    @Override
    public PermissionDTO findById(Long id) throws PermissionNotFoundException {
        Permission permission = permissionDAO.findPermissionByIdAndIsNotDelete(id);

        if(permission == null) throw new PermissionNotFoundException();

        return permissionMapper.mapFromEntity(permission);
    }

    @Override
    public void create(PermissionDTO permissionDTO) throws PermissionAlreadyExistsException {
        boolean permissionExists = permissionDAO.existsByName(permissionDTO.getName());

        if(Boolean.TRUE.equals(permissionExists)) throw new PermissionAlreadyExistsException(permissionDTO.getName());

        Permission permission = new Permission();
        permission = permissionMapper.mapToEntity(permissionDTO, permission);
        permissionDAO.save(permission);
    }

    @Override
    public void update(Long permissionId, PermissionDTO permissionDTO) throws PermissionAlreadyExistsException, PermissionNotFoundException {
        Permission permission  = this.findByName(permissionDTO.getName());
        if(!permission.getId().equals(permissionId)) throw new PermissionAlreadyExistsException(permissionDTO.getName());

        permission = permissionDAO.findById(permissionId).orElseThrow(PermissionNotFoundException::new);
        permission = permissionMapper.mapToEntity(permissionDTO, permission);
        permission.setUpdatedAt(LocalDateTime.now());
        permissionDAO.save(permission);
    }

    @Override
    public void delete(Long permissionId) throws PermissionNotFoundException {
        Permission permission = permissionDAO.findById(permissionId).orElseThrow(PermissionNotFoundException::new);
        permission.setDelete(1);
        permission.setActive(0);
        permission.setUpdatedAt(LocalDateTime.now());
        permission.setDeletedAt(LocalDateTime.now());
        permissionDAO.save(permission);
    }
}
