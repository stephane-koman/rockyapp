package com.rockyapp.rockyappbackend.roles.controller;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.exception.RoleAlreadyExistsException;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/role")
public class RoleController {

    private RoleService roleService;

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('SEARCH_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public ResultPagine<RoleDTO> searchRoles(@RequestParam(name = "name", defaultValue = "") String name,
                                        @RequestParam(name = "active", defaultValue = "2") int active,
                                        Pageable pageable){
        return roleService.searchRoleByNameAndIsNotDelete(name, active, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAuthority('SEARCH_ROLE')")
    public RoleDTO findRoleById(@PathVariable(name = "id") Long id) throws RoleNotFoundException {
        return roleService.findRoleById(id);
    }

    @PostMapping
    @PostAuthorize("hasAuthority('CREATE_ROLE')")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) throws RoleAlreadyExistsException {
        return roleService.create(roleDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAuthority('UPDATE_ROLE')")
    public RoleDTO updateRole(@PathVariable(name = "id") Long id, @RequestBody RoleDTO roleDTO) throws RoleAlreadyExistsException, RoleNotFoundException {
        return roleService.update(id,roleDTO);
    }

    @DeleteMapping
    @PostAuthorize("hasAuthority('DELETE_ROLE')")
    public void deleteRole(@RequestParam(name = "id") Long id) throws RoleNotFoundException {
        roleService.delete(id);
    }
}
