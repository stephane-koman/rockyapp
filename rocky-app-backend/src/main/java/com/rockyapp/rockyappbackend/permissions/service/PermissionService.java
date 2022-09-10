package com.rockyapp.rockyappbackend.permissions.service;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;

public interface PermissionService {
    Permission findPermissionByName(final String name) throws PermissionNotFoundException;
}
