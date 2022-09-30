package com.rockyapp.rockyappbackend.users.builder;

import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;

import java.util.Arrays;

public class UserBuilder {

    public static UserDTO getDto() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setName("ADMIN");
        dto.setRoleList(Arrays.asList("A", "B"));
        return dto;
    }

    public static User getEntity() {
        User entity = new User();
        entity.setId(1L);
        entity.setName("ADMIN");
        entity.setRoles(
                Arrays.asList(
                        new Role(1L, "A", null, null, null),
                        new Role(2L, "B", null, null, null))
        );
        return entity;
    }
}