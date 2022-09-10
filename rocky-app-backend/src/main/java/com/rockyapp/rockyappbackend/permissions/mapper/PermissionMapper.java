package com.rockyapp.rockyappbackend.permissions.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper extends AbstractSocleMapper<Permission, PermissionDTO> implements SocleMapper<Permission, PermissionDTO> {
    @Override
    public Permission mapToEntity(PermissionDTO model, Permission entity) {
        BeanUtils.copyProperties(model, entity, "id");
        return entity;
    }

    @Override
    public PermissionDTO mapFromEntity(Permission entity) {
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(entity, permissionDTO, "active", "roles", "users");
        permissionDTO.setActive(entity.getActive() == 1);
        return permissionDTO;
    }
}
