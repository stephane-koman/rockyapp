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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    
    private UserService userService;

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public ResponseEntity<ResultPagine<SimpleUserDTO>> search(@RequestBody(required = false) UserSearchCriteriaDTO criteriaDTO,
                                      Pageable pageable){
        ResultPagine<SimpleUserDTO> resultPagine = userService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(resultPagine);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public ResponseEntity<UserDTO> findById(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_USER', 'UPDATE_USER', 'DELETE_USER')")
    public ResponseEntity<Void> create(@RequestBody UserCreaDTO userDTO) throws UsernameAlreadyExistsException, PasswordNotMatchException, EmailAlreadyExistsException, PasswordEmptyException {
        userService.create(userDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_USER', 'DELETE_USER')")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id, @RequestBody UserCreaDTO userDTO) throws UserNotFoundException, UsernameAlreadyExistsException, PasswordNotMatchException, EmailAlreadyExistsException, PasswordEmptyException {
        userService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
