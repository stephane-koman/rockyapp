package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.SocleMapper;
import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import com.rockyapp.rockyappbackend.permissions.dao.PermissionDAO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
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

    private RoleDAO roleDAO;
    private PermissionDAO permissionDAO;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User mapToEntity(UserCreaDTO model, User entity) throws NotFoundException {
        BeanUtils.copyProperties(model, entity, "id", "password", "role", "permissions");

        if(!model.getPassword().equals(model.getPasswordConfirm()))
            throw new NotFoundException();

        String hashPW = bCryptPasswordEncoder.encode(model.getPassword());
        entity.setPassword(hashPW);

        if(model.getRole() != null) {
            Role role = roleDAO.findById(model.getRole()).orElseThrow(NotFoundException::new);
            entity.setRole(role);
        }

        if (!model.getPermissionList().isEmpty()){
            List<Permission> permissionList = new ArrayList<>();
            model.getPermissionList().forEach(p -> {
                Permission permission = permissionDAO.findPermissionByName(p);
                if(permission != null) permissionList.add(permission);
            });
            entity.getPermissions().addAll(permissionList);
        }

        return entity;
    }

    @Override
    public UserCreaDTO mapFromEntity(User entity) {
        UserCreaDTO userCreaDTO = new UserCreaDTO();
        BeanUtils.copyProperties(entity, userCreaDTO, "role", "permissions");
        userCreaDTO.setRole(entity.getRole().getId());
        userCreaDTO.setPermissionList(entity.getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));

        return null;
    }
}
