package com.rockyapp.rockyappbackend.utils.mappers;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import com.rockyapp.rockyappbackend.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserGlobalMapper {

    private RoleService roleService;
    private PermissionService permissionService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void encryptPassword(String password, User entity) {
        String hashPW = bCryptPasswordEncoder.encode(password);
        entity.setPassword(hashPW);
    }

    public void mapRoles(List<String> roles, User entity) {
        if(roles != null && !roles.isEmpty()){
            List<Role> roleList = new ArrayList<>();
            roles.forEach(r -> {
                Role role = null;
                try {
                    role = roleService.findRoleByName(r);
                } catch (RoleNotFoundException e) {
                    throw new RuntimeException(e);
                }
                roleList.add(role);
            });
            entity.getRoles().addAll(roleList);
        }
    }

    public void mapPermissions(List<String> permissions, User entity) {
        if (permissions != null && !permissions.isEmpty()){
            List<Permission> permissionList = new ArrayList<>();
            permissions.forEach(p -> {
                Permission permission = null;
                try {
                    permission = permissionService.findPermissionByName(p);
                } catch (PermissionNotFoundException e) {
                    throw new RuntimeException(e);
                }
                permissionList.add(permission);
            });
            entity.getPermissions().addAll(permissionList);
        }
    }
}