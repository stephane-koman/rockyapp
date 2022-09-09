package com.rockyapp.rockyappbackend.permissions.dto;

import com.rockyapp.rockyappbackend.common.SocleDTO;

public class PermissionDTO implements SocleDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
}
