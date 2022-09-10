package com.rockyapp.rockyappbackend.users.service.impl;

import com.rockyapp.rockyappbackend.users.dao.UserDAO;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;
import com.rockyapp.rockyappbackend.users.mapper.UserCreaMapper;
import com.rockyapp.rockyappbackend.users.mapper.UserMapper;
import com.rockyapp.rockyappbackend.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private UserCreaMapper userCreaMapper;
    private UserMapper userMapper;

    @Override
    public UserDTO create(UserCreaDTO userCreaDTO) throws PasswordNotMatchException {
        User user = new User();
        user = userDAO.save(userCreaMapper.mapToEntity(userCreaDTO, user));
        return userMapper.mapFromEntity(user);
    }

    @Override
    public User findUserByUsernameOrEmail(String name) throws UserNotFoundException {
        User user = userDAO.findUserByUsernameOrEmailAndIsActiveAndIsNotDelete(name);

        if(user == null) throw new UserNotFoundException();

        return user;
    }
}
