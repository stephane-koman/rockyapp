package com.rockyapp.rockyappbackend.roles.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRoleDTO implements SocleDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
}
