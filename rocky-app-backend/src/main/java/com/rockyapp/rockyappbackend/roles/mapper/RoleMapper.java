package com.rockyapp.rockyappbackend.roles.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.utils.mappers.UserGlobalMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoleMapper extends AbstractSocleMapper<Role, RoleDTO> implements SocleMapper<Role, RoleDTO> {

    private UserGlobalMapper userGlobalMapper;

    @Override
    public Role mapToEntity(RoleDTO model, Role entity) {
        BeanUtils.copyProperties(model, entity, "id", "active", "permissionList");
        entity.setActive(model.isActive() ? 1 : 0);

        userGlobalMapper.mapPermissions(model.getPermissionList(), entity.getPermissions());

        return entity;
    }

    @Override
    public RoleDTO mapFromEntity(Role entity) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(entity, roleDTO, "active", "permissions");
        roleDTO.setActive(entity.getActive() == 1);

        if(!entity.getPermissions().isEmpty())
            roleDTO.setPermissionList(entity.getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));

        return roleDTO;
    }
}
