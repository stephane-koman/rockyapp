package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.PasswordEmptyException;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import com.rockyapp.rockyappbackend.utils.mappers.UserGlobalMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper extends AbstractSocleMapper<User, UserDTO> implements SocleMapper<User, UserDTO> {

    private UserGlobalMapper userGlobalMapper;

    @Override
    public User mapToEntity(UserDTO model, User entity) throws PasswordNotMatchException, PasswordEmptyException {
        BeanUtils.copyProperties(model, entity, "active", "password", "passwordConfirm", "roleList", "permissionList");
        entity.setActive(model.isActive() ? 1 : 0);
        userGlobalMapper.mapRoles(model.getRoleList(), entity.getRoles());
        userGlobalMapper.mapPermissions(model.getPermissionList(), entity.getPermissions());

        return entity;
    }

    @Override
    public UserDTO mapFromEntity(User entity) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(entity, userDTO, "active", "roles", "permissions");

        userDTO.setActive(entity.getActive() == 1);
        userDTO.setRoleList(entity.getRoles().stream().filter(r -> r.getActive() == 1 && r.getDelete() == 0).map(Role::getName).collect(Collectors.toList()));
        userDTO.setPermissionList(entity.getPermissions().stream().filter(p -> p.getActive() == 1 && p.getDelete() == 0).map(Permission::getName).collect(Collectors.toList()));

        return userDTO;
    }
}
