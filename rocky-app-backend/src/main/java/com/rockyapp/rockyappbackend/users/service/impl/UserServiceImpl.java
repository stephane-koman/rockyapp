package com.rockyapp.rockyappbackend.users.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.users.dao.UserDAO;
import com.rockyapp.rockyappbackend.users.dto.SimpleUserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.*;
import com.rockyapp.rockyappbackend.users.mapper.SimpleUserMapper;
import com.rockyapp.rockyappbackend.users.mapper.UserCreaMapper;
import com.rockyapp.rockyappbackend.users.mapper.UserMapper;
import com.rockyapp.rockyappbackend.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private UserCreaMapper userCreaMapper;
    private UserMapper userMapper;
    private SimpleUserMapper simpleUserMapper;

    @Override
    public User findByUsernameOrEmail(String name) throws UserNotFoundException {
        User user = userDAO.findUserByUsernameOrEmailAndIsActiveAndIsNotDelete(name);

        if(user == null) throw new UserNotFoundException();

        return user;
    }

    @Override
    public UserDTO findUserById(Long id) throws UserNotFoundException {
        User user = userDAO.findUserByIdAndIsNotDelete(id);

        if(user == null) throw new UserNotFoundException();

        return userMapper.mapFromEntity(user);
    }

    @Override
    public ResultPagine<SimpleUserDTO> search(UserSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<User> userPage = userDAO.searchUsers(criteriaDTO, pageable);
        return simpleUserMapper.mapFromEntity(userPage);
    }

    @Override
    public UserDTO create(UserCreaDTO userCreaDTO) throws PasswordNotMatchException, PasswordEmptyException, UsernameAlreadyExistsException, EmailAlreadyExistsException {
        User usernameExists = userDAO.findUserByUsernameOrEmailAndIsActiveAndIsNotDelete(userCreaDTO.getUsername());
        User emailExists = userDAO.findUserByUsernameOrEmailAndIsActiveAndIsNotDelete(userCreaDTO.getEmail());

        if(usernameExists != null) throw new UsernameAlreadyExistsException(userCreaDTO.getUsername());
        if(emailExists != null) throw new EmailAlreadyExistsException(userCreaDTO.getEmail());

        User user = new User();
        userDAO.save(userCreaMapper.mapToEntity(userCreaDTO, user));
    }

    @Override
    public void update(Long userId, UserCreaDTO userCreaDTO) throws PasswordNotMatchException, PasswordEmptyException, UsernameAlreadyExistsException, EmailAlreadyExistsException, UserNotFoundException {
        User userName = this.findByUsernameOrEmail(userCreaDTO.getUsername());
        User userEmail = this.findByUsernameOrEmail(userCreaDTO.getEmail());

        if(!userName.getId().equals(userId)) throw new UsernameAlreadyExistsException(userCreaDTO.getUsername());
        if(!userEmail.getId().equals(userId)) throw new EmailAlreadyExistsException(userCreaDTO.getUsername());

        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        user = userCreaMapper.mapToEntity(userCreaDTO, user);
        user.setUpdatedAt(LocalDateTime.now());
        user = userDAO.save(user);

        return userMapper.mapFromEntity(user);
    }

    @Override
    public void delete(Long userId) throws UserNotFoundException {
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setDelete(1);
        user.setActive(0);
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeletedAt(LocalDateTime.now());
        userDAO.save(user);
    }
}
