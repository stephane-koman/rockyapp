package com.rockyapp.rockyappbackend.permissions.service.impl;

import com.rockyapp.rockyappbackend.permissions.dao.PermissionDAO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private PermissionDAO permissionDAO;

    @Override
    public Permission findPermissionByName(String name) throws PermissionNotFoundException {
        Permission permission = permissionDAO.findPermissionByNameAndIsActiveAndIsNotDelete(name);

        if(permission == null) throw new PermissionNotFoundException();

        return permission;
    }
}
