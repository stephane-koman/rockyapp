package com.rockyapp.rockyappbackend.roles.dto;

import com.rockyapp.rockyappbackend.common.SocleDTO;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements SocleDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;

    private List<PermissionDTO> permissionList;
}
