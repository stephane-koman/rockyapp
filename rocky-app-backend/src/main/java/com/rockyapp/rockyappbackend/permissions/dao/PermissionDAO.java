package com.rockyapp.rockyappbackend.permissions.dao;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PermissionDAO extends PagingAndSortingRepository<Permission, Long> {
    Permission findPermissionByName(final String name);
}
