package com.rockyapp.rockyappbackend.permissions.controller;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionAlreadyExistsException;
import com.rockyapp.rockyappbackend.permissions.exception.PermissionNotFoundException;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {

    private PermissionService permissionService;

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public ResponseEntity<ResultPagine<SimplePermissionDTO>> search(@RequestBody(required = false) DefaultCriteriaDTO criteriaDTO,
                                                               Pageable pageable){
        ResultPagine<SimplePermissionDTO> resultPagine = permissionService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(resultPagine);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public ResponseEntity<PermissionDTO> findById(@PathVariable(name = "id") Long id) throws PermissionNotFoundException {
        PermissionDTO permissionDTO = permissionService.findById(id);
        return ResponseEntity.ok(permissionDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public ResponseEntity<Void> create(@RequestBody @Validated PermissionDTO permissionDTO) throws PermissionAlreadyExistsException {
        permissionService.create(permissionDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_PERMISSION', 'DELETE_PERMISSION')")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id, @RequestBody PermissionDTO permissionDTO) throws PermissionNotFoundException, PermissionAlreadyExistsException {
        permissionService.update(id, permissionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_PERMISSION')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) throws PermissionNotFoundException {
        permissionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
