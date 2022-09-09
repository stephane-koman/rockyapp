package com.rockyapp.rockyappbackend.users.dto;

import com.rockyapp.rockyappbackend.common.SocleDTO;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreaDTO implements SocleDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String passwordConfirm;

    private int isActive;

    private Long role = null;
    private List<String> permissionList = new ArrayList<>();
}
