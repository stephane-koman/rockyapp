package com.rockyapp.rockyappbackend.users.service;

import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.entity.User;

public interface UserService {
    UserDTO create(UserCreaDTO userCreaDTO) throws NotFoundException;
    User findUserByUsername(final String username) throws NotFoundException;
}
