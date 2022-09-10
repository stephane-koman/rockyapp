package com.rockyapp.rockyappbackend.users.service;

import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;

public interface UserService {
    UserDTO create(UserCreaDTO userCreaDTO) throws PasswordNotMatchException;
    User findUserByUsernameOrEmail(final String name) throws UserNotFoundException;
}
