package com.rockyapp.rockyappbackend.users.controller;

import com.rockyapp.rockyappbackend.common.dto.StatusDTO;
import com.rockyapp.rockyappbackend.common.exception.InvalidTokenException;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.security.SecurityConstants;
import com.rockyapp.rockyappbackend.users.dto.*;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.*;
import com.rockyapp.rockyappbackend.users.mapper.UserMapper;
import com.rockyapp.rockyappbackend.users.service.UserService;
import com.rockyapp.rockyappbackend.utils.helpers.TokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    
    private UserService userService;
    private UserMapper userMapper;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public ResultPagine<SimpleUserDTO> searchUsers(@RequestBody(required = false) UserSearchCriteriaDTO criteriaDTO,
                                                   Pageable pageable){
        return userService.searchUsers(criteriaDTO, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public UserDTO findUserById(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userService.findUserById(id);
    }

    @GetMapping
    public UserDTO getUserConnectedInfos(HttpServletRequest request) throws InvalidTokenException, UserNotFoundException {
        String authToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if(authToken == null || !authToken.startsWith(SecurityConstants.TOKEN_PREFIX))
            throw new InvalidTokenException("");

        String username = TokenHelper.extractUsernameFromToken(authToken);

        User user = userService.findUserByUsernameOrEmail(username);
        return userMapper.mapFromEntity(user);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public UserDTO createUser(@RequestBody UserCreaDTO userDTO) throws UsernameAlreadyExistsException, PasswordNotMatchException, EmailAlreadyExistsException, PasswordEmptyException {
        return userService.create(userDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_USER', 'DELETE_USER')")
    public UserDTO updateUser(@PathVariable(name = "id") Long id, @RequestBody UserUpdateDTO userDTO) throws UserNotFoundException, UsernameAlreadyExistsException, PasswordNotMatchException, EmailAlreadyExistsException, PasswordEmptyException {
        return userService.update(id, userDTO);
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_USER', 'DELETE_USER')")
    public void updateUserStatus(@PathVariable(name = "id") Long id, @RequestBody StatusDTO statusDTO) throws UserNotFoundException {
        userService.changeUserStatus(id, statusDTO.isActive());
    }

    @PutMapping("/reset_password/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_USER', 'DELETE_USER')")
    public UserDTO initUserPassword(@PathVariable(name = "id") Long id, @RequestBody PasswordDTO passwordDTO) throws UserNotFoundException, PasswordNotMatchException, PasswordEmptyException {
        return userService.initPassword(id, passwordDTO);
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_USER')")
    public void deleteUser(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        userService.delete(id);
    }
}
