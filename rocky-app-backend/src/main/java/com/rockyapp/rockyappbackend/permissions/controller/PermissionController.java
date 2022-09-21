package com.rockyapp.rockyappbackend.permissions.controller;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.dto.StatusDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionAlreadyExistsException;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {

    private PermissionService permissionService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public ResultPagine<SimplePermissionDTO> searchPermissions(@RequestBody(required = false) DefaultCriteriaDTO criteriaDTO,
                                                               Pageable pageable){
        return permissionService.searchPermissions(criteriaDTO, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public PermissionDTO findPermissionById(@PathVariable(name = "id") Long id) throws PermissionNotFoundException {
        return permissionService.findPermissionById(id);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public PermissionDTO createPermission(@RequestBody PermissionDTO permissionDTO) throws PermissionAlreadyExistsException {
        return permissionService.create(permissionDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public PermissionDTO updatePermission(@PathVariable(name = "id") Long id, @RequestBody PermissionDTO permissionDTO) throws PermissionNotFoundException, PermissionAlreadyExistsException {
        return permissionService.update(id, permissionDTO);
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public void updateUserStatus(@PathVariable(name = "id") Long id, @RequestBody StatusDTO statusDTO) throws PermissionNotFoundException {
        permissionService.changePermissionStatus(id, statusDTO.isActive());
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_PERMISSION')")
    public void deletePermission(@PathVariable(name = "id") Long id) throws PermissionNotFoundException {
        permissionService.delete(id);
    }
}
