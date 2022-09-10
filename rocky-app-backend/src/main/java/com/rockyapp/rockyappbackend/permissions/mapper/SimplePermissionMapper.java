package com.rockyapp.rockyappbackend.permissions.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimplePermissionMapper extends AbstractSocleMapper<Permission, SimplePermissionDTO> implements SocleMapper<Permission, SimplePermissionDTO> {

    @Override
    public Permission mapToEntity(SimplePermissionDTO model, Permission entity) {
        return null;
    }

    @Override
    public SimplePermissionDTO mapFromEntity(Permission entity) {
        SimplePermissionDTO permissionDTO = new SimplePermissionDTO();
        BeanUtils.copyProperties(entity, permissionDTO, "active");
        permissionDTO.setActive(entity.getActive() == 1);
        return permissionDTO;
    }
}
