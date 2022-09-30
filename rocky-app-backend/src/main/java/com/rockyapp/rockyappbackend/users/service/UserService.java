package com.rockyapp.rockyappbackend.users.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.users.dto.*;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.*;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ResultPagine<SimpleUserDTO> search(UserSearchCriteriaDTO  criteriaDTO, final Pageable pageable);
    User findByUsernameOrEmail(final String name) throws UserNotFoundException;
    UserDTO findById(final Long id) throws UserNotFoundException;
    void create(UserCreaDTO userCreaDTO) throws PasswordNotMatchException, PasswordEmptyException, UsernameAlreadyExistsException, EmailAlreadyExistsException;
    void update(Long userId, UserUpdateDTO userUpdateDTO) throws PasswordNotMatchException, PasswordEmptyException, UsernameAlreadyExistsException, EmailAlreadyExistsException, UserNotFoundException;
    void initPassword(Long userId, PasswordDTO passwordDTO) throws PasswordNotMatchException, PasswordEmptyException, UserNotFoundException;
    void delete(Long userId) throws UserNotFoundException;
    void changeUserStatus(Long userId, boolean status) throws UserNotFoundException;
}
