package com.rockyapp.rockyappbackend.users.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.PasswordEmptyException;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import com.rockyapp.rockyappbackend.utils.mappers.UserGlobalMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCreaMapper extends AbstractSocleMapper<User, UserCreaDTO> implements SocleMapper<User, UserCreaDTO> {
    private UserGlobalMapper userGlobalMapper;

    @Override
    public User mapToEntity(UserCreaDTO model, User entity) throws PasswordNotMatchException, PasswordEmptyException {
        BeanUtils.copyProperties(model, entity, "password", "passwordConfirm", "roleList", "permissionList");

        if(entity.getId() == null){
            if(model.getPassword() == null || model.getPasswordConfirm() == null)
                throw new PasswordEmptyException();

            if(!model.getPassword().equals(model.getPasswordConfirm()))
                throw new PasswordNotMatchException();

            userGlobalMapper.encryptPassword(model.getPassword(), entity);
        }else{
            if(
                    model.getPassword() != null &&
                            model.getPasswordConfirm() != null &&
                            !model.getPassword().equals(model.getPasswordConfirm())
            )
                throw new PasswordNotMatchException();

            if(
                    (model.getPassword() != null && model.getPasswordConfirm() == null) ||
                    (model.getPassword() == null && model.getPasswordConfirm() != null)
            )
                throw new PasswordEmptyException();

            if(model.getPassword() != null && model.getPasswordConfirm() != null) userGlobalMapper.encryptPassword(model.getPassword(), entity);

        }

        userGlobalMapper.mapRoles(model.getRoleList(), entity);
        userGlobalMapper.mapPermissions(model.getPermissionList(), entity.getPermissions());

        return entity;
    }

    @Override
    public UserCreaDTO mapFromEntity(User entity) {
        return null;
    }
}
