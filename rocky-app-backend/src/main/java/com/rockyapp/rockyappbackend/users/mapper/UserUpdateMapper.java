package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.users.dto.UserUpdateDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.utils.mappers.UserGlobalMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUpdateMapper extends AbstractSocleMapper<User, UserUpdateDTO> implements SocleMapper<User, UserUpdateDTO> {
    private UserGlobalMapper userGlobalMapper;

    @Override
    public User mapToEntity(UserUpdateDTO model, User entity) {
        BeanUtils.copyProperties(model, entity,  "id", "active", "roleList", "permissionList");
        entity.setActive(model.isActive() ? 1 : 0);

        userGlobalMapper.mapRoles(model.getRoleList(), entity.getRoles());
        userGlobalMapper.mapPermissions(model.getPermissionList(), entity.getPermissions());

        return entity;
    }

    @Override
    public UserUpdateDTO mapFromEntity(User entity) {
        return null;
    }
}
