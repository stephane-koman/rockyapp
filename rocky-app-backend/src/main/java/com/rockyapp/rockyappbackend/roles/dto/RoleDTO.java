package com.rockyapp.rockyappbackend.roles.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements SocleDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active = true;

    private List<String> permissionList = new ArrayList<>();
}
