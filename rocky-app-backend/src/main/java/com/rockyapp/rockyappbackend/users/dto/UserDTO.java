package com.rockyapp.rockyappbackend.users.dto;

import com.rockyapp.rockyappbackend.common.SocleDTO;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements SocleDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String passwordConfirm;

    private int isActive;

    private SimpleRoleDTO role = null;
    private List<String> permissionList = new ArrayList<>();
}
