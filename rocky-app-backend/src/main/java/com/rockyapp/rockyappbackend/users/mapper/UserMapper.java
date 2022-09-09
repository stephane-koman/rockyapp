package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.SocleMapper;
import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import com.rockyapp.rockyappbackend.permissions.dao.PermissionDAO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.mapper.SimpleRoleMapper;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper extends AbstractSocleMapper<User, UserDTO> implements SocleMapper<User, UserDTO> {

    private SimpleRoleMapper roleMapper;

    @Override
    public User mapToEntity(UserDTO model, User entity) throws NotFoundException {
        return null;
    }

    @Override
    public UserDTO mapFromEntity(User entity) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(entity, userDTO, "role", "permissions");
        userDTO.setRole(roleMapper.mapFromEntity(entity.getRole()));
        userDTO.setPermissionList(entity.getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));

        return null;
    }
}
