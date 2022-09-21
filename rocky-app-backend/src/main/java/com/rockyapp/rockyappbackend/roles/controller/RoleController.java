package com.rockyapp.rockyappbackend.roles.controller;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import com.rockyapp.rockyappbackend.roles.exception.RoleAlreadyExistsException;
import com.rockyapp.rockyappbackend.roles.exception.RoleNotFoundException;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/role")
public class RoleController {

    private RoleService roleService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public ResultPagine<SimpleRoleDTO> searchRoles(@RequestBody(required = false) DefaultCriteriaDTO criteriaDTO,
                                                   Pageable pageable){
        return roleService.searchRoles(criteriaDTO, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public RoleDTO findRoleById(@PathVariable(name = "id") Long id) throws RoleNotFoundException {
        return roleService.findRoleById(id);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) throws RoleAlreadyExistsException {
        return roleService.create(roleDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_ROLE', 'DELETE_ROLE')")
    public RoleDTO updateRole(@PathVariable(name = "id") Long id, @RequestBody RoleDTO roleDTO) throws RoleAlreadyExistsException, RoleNotFoundException {
        return roleService.update(id,roleDTO);
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_ROLE')")
    public void deleteRole(@PathVariable(name = "id") Long id) throws RoleNotFoundException {
        roleService.delete(id);
    }
}
