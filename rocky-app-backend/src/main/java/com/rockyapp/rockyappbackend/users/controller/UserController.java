package com.rockyapp.rockyappbackend.users.controller;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.users.dto.SimpleUserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.users.exception.*;
import com.rockyapp.rockyappbackend.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    
    private UserService userService;

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('SEARCH_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public ResultPagine<SimpleUserDTO> searchUsers(@RequestBody(required = false) UserSearchCriteriaDTO criteriaDTO,
                                                   Pageable pageable){
        return userService.searchUsers(criteriaDTO, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('SEARCH_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public UserDTO findUserById(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userService.findUserById(id);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public UserDTO createUser(@RequestBody UserCreaDTO userDTO) throws UsernameAlreadyExistsException, PasswordNotMatchException, EmailAlreadyExistsException, PasswordEmptyException {
        return userService.create(userDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_USER', 'DELETE_USER')")
    public UserDTO updateUser(@PathVariable(name = "id") Long id, @RequestBody UserCreaDTO userDTO) throws UserNotFoundException, UsernameAlreadyExistsException, PasswordNotMatchException, EmailAlreadyExistsException, PasswordEmptyException {
        return userService.update(id, userDTO);
    }

    @DeleteMapping
    @PostAuthorize("hasAuthority('DELETE_USER')")
    public void deleteUser(@RequestParam(name = "id") Long id) throws UserNotFoundException {
        userService.delete(id);
    }
}
