package com.rockyapp.rockyappbackend.roles.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.mapper.PermissionMapper;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoleMapper extends AbstractSocleMapper<Role, RoleDTO> implements SocleMapper<Role, RoleDTO> {

    private PermissionService permissionService;
    private PermissionMapper permissionMapper;

    @Override
    public Role mapToEntity(RoleDTO model, Role entity) {
        BeanUtils.copyProperties(model, entity, "id", "permissionList");
        if(!model.getPermissionList().isEmpty()){
            entity.getPermissions().clear();
            List<Permission> permissionList = model.getPermissionList().stream().map(p -> {
                try {
                    return permissionService.findPermissionByName(p);
                } catch (PermissionNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            entity.getPermissions().addAll(permissionList);
        }

        return entity;
    }

    @Override
    public RoleDTO mapFromEntity(Role entity) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(entity, roleDTO, "active");
        roleDTO.setActive(entity.getActive() == 1);

        if(!entity.getPermissions().isEmpty())
            roleDTO.setPermissionList(entity.getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));

        return roleDTO;
    }
}
