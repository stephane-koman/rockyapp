package com.rockyapp.rockyappbackend.roles.mapper;

import com.rockyapp.rockyappbackend.common.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.mapper.PermissionMapper;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleMapper extends AbstractSocleMapper<Role, RoleDTO> implements SocleMapper<Role, RoleDTO> {

    private PermissionMapper permissionMapper;

    @Override
    public Role mapToEntity(RoleDTO model, Role entity) {
        BeanUtils.copyProperties(model, entity, "id", "permissionList");
        return entity;
    }

    @Override
    public RoleDTO mapFromEntity(Role entity) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(entity, roleDTO, "active");
        roleDTO.setActive(entity.getActive() == 1);
        roleDTO.setPermissionList(permissionMapper.mapFromEntity(entity.getPermissions()));
        return roleDTO;
    }
}
