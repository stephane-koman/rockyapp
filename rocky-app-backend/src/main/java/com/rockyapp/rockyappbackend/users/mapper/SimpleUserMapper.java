package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.users.dto.SimpleUserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleUserMapper extends AbstractSocleMapper<User, SimpleUserDTO> implements SocleMapper<User, SimpleUserDTO> {

    @Override
    public User mapToEntity(SimpleUserDTO model, User entity) {
        return null;
    }

    @Override
    public SimpleUserDTO mapFromEntity(User entity) {
        SimpleUserDTO userDTO = new SimpleUserDTO();
        BeanUtils.copyProperties(entity, userDTO, "active", "permissions", "roles");
        userDTO.setActive(entity.getActive() == 1);
        return userDTO;
    }
}
