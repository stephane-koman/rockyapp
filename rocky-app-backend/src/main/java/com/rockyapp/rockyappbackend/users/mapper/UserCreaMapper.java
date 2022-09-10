package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserCreaMapper extends AbstractSocleMapper<User, UserCreaDTO> implements SocleMapper<User, UserCreaDTO> {

    private RoleService roleService;
    private PermissionService permissionService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User mapToEntity(UserCreaDTO model, User entity) throws PasswordNotMatchException {
        BeanUtils.copyProperties(model, entity, "password", "passwordConfirm", "roleList", "permissionList");

        if(!model.getPassword().equals(model.getPasswordConfirm()))
            throw new PasswordNotMatchException();

        String hashPW = bCryptPasswordEncoder.encode(model.getPassword());
        entity.setPassword(hashPW);

        if(model.getRoleList() != null && model.getRoleList().isEmpty()) {
            List<Role> roleList = new ArrayList<>();
            model.getRoleList().forEach(r -> {
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

        if (model.getPermissionList() != null && !model.getPermissionList().isEmpty()){
            List<Permission> permissionList = new ArrayList<>();
            model.getPermissionList().forEach(p -> {
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

        return entity;
    }

    @Override
    public UserCreaDTO mapFromEntity(User entity) {
        UserCreaDTO userCreaDTO = new UserCreaDTO();
        BeanUtils.copyProperties(entity, userCreaDTO, "roles", "permissions");
        userCreaDTO.setRoleList(entity.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        userCreaDTO.setPermissionList(entity.getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));

        return null;
    }
}
