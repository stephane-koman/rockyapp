package com.rockyapp.rockyappbackend.permissions.controller;

import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;

import java.util.Collections;
import java.util.List;

public class PermissionBuilder {

    public static PermissionDTO getDto() {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(1L);
        return dto;
    }

    public static Permission getEntity() {
        Permission entity = new Permission();
        entity.setId(1L);
        entity.setName("toto");
        return entity;
    }
}