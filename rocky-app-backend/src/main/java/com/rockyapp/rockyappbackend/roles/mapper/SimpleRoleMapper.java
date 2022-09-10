package com.rockyapp.rockyappbackend.roles.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleRoleMapper extends AbstractSocleMapper<Role, SimpleRoleDTO> implements SocleMapper<Role, SimpleRoleDTO> {

    @Override
    public Role mapToEntity(SimpleRoleDTO model, Role entity) {
        return null;
    }

    @Override
    public SimpleRoleDTO mapFromEntity(Role entity) {
        SimpleRoleDTO roleDTO = new SimpleRoleDTO();
        BeanUtils.copyProperties(entity, roleDTO, "active", "permissions");
        roleDTO.setActive(entity.getActive() == 1);
        return roleDTO;
    }
}
