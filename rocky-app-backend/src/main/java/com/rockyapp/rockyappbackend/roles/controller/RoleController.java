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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/role")
public class RoleController {

    private RoleService roleService;

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public ResponseEntity<ResultPagine<SimpleRoleDTO>> search(@RequestBody(required = false) DefaultCriteriaDTO criteriaDTO,
                                      Pageable pageable){
        ResultPagine<SimpleRoleDTO> resultPagine = roleService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(resultPagine);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public ResponseEntity<RoleDTO> findById(@PathVariable(name = "id") Long id) throws RoleNotFoundException {
        RoleDTO roleDTO = roleService.findById(id);
        return ResponseEntity.ok(roleDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE')")
    public ResponseEntity<Void> create(@RequestBody RoleDTO roleDTO) throws RoleAlreadyExistsException {
        roleService.create(roleDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_ROLE', 'DELETE_ROLE')")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id, @RequestBody RoleDTO roleDTO) throws RoleAlreadyExistsException, RoleNotFoundException {
        roleService.update(id,roleDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) throws RoleNotFoundException {
        roleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
