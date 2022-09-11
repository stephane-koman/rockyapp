package com.rockyapp.rockyappbackend.users.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.users.dto.SimpleUserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.*;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ResultPagine<SimpleUserDTO> searchUsers(UserSearchCriteriaDTO  criteriaDTO, final Pageable pageable);
    User findUserByUsernameOrEmail(final String name) throws UserNotFoundException;
    UserDTO findUserById(final Long id) throws UserNotFoundException;
    UserDTO create(UserCreaDTO userCreaDTO) throws PasswordNotMatchException, PasswordEmptyException, UsernameAlreadyExistsException, EmailAlreadyExistsException;
    UserDTO update(Long userId, UserCreaDTO userCreaDTO) throws PasswordNotMatchException, PasswordEmptyException, UsernameAlreadyExistsException, EmailAlreadyExistsException, UserNotFoundException;
    void delete(Long userId) throws UserNotFoundException;
}
