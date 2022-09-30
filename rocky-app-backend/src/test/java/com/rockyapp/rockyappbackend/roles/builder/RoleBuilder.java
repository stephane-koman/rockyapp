package com.rockyapp.rockyappbackend.roles.builder;

import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;

public class RoleBuilder {

    public static RoleDTO getDto() {
        RoleDTO dto = new RoleDTO();
        dto.setId(1L);
        dto.setName("ADMIN");
        return dto;
    }

    public static Role getEntity() {
        Role entity = new Role();
        entity.setId(1L);
        entity.setName("ADMIN");
        return entity;
    }
}