package com.rockyapp.rockyappbackend.utils.mappers;

import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import com.rockyapp.rockyappbackend.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserGlobalMapper {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void encryptPassword(String password, User entity) {
        String hashPW = bCryptPasswordEncoder.encode(password);
        entity.setPassword(hashPW);
    }

    public void mapRoles(List<String> roles, List<Role> oldRoles) {
        oldRoles.clear();
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
            oldRoles.addAll(roleList);
        }
    }

    public void mapPermissions(List<String> permissions, List<Permission> oldPermissions) {
        oldPermissions.clear();
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
            oldPermissions.addAll(permissionList);
        }
    }
}
