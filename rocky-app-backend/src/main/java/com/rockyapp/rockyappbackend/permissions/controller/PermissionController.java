package com.rockyapp.rockyappbackend.permissions.controller;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionAlreadyExistsException;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/permission")
public class PermissionController {

    private PermissionService permissionService;

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('SEARCH_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public ResultPagine<SimplePermissionDTO> searchPermissions(@RequestParam(name = "name", defaultValue = "") String name,
                                                               @RequestParam(name = "active", defaultValue = "2") int active,
                                                               Pageable pageable){
        return permissionService.searchPermissionByNameAndIsNotDelete(name, active, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('SEARCH_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
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

    @DeleteMapping
    @PostAuthorize("hasAuthority('DELETE_PERMISSION')")
    public void deletePermission(@RequestParam(name = "id") Long id) throws PermissionNotFoundException {
        permissionService.delete(id);
    }
}
