package com.rockyapp.rockyappbackend.permissions.mapper;

import com.rockyapp.rockyappbackend.common.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.SocleMapper;
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
        PermissionDTO PermissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(entity, PermissionDTO);
        return null;
    }
}
