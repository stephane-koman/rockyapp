package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.dto.SimpleUserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.utils.mappers.UserGlobalMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SimpleUserMapper extends AbstractSocleMapper<User, SimpleUserDTO> implements SocleMapper<User, SimpleUserDTO> {

    private UserGlobalMapper userGlobalMapper;

    @Override
    public User mapToEntity(SimpleUserDTO model, User entity) {
        return null;
    }

    @Override
    public SimpleUserDTO mapFromEntity(User entity) {
        SimpleUserDTO userDTO = new SimpleUserDTO();
        BeanUtils.copyProperties(entity, userDTO, "active", "permissions", "roles");
        userDTO.setActive(entity.getActive() == 1);

        userDTO.setRoleList(entity.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return userDTO;
    }
}
