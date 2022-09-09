package com.rockyapp.rockyappbackend.roles.controller;

import com.rockyapp.rockyappbackend.common.ResultPagine;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/role")
public class RoleController {

    private RoleService roleService;

    @GetMapping("/search")
    public ResultPagine<RoleDTO> search(@RequestParam(name = "name", defaultValue = "") String name,
                                        @RequestParam(name = "active", defaultValue = "2") int active,
                                        Pageable pageable){
        return roleService.searchRoleByNameAndIsNotDelete(name, active, pageable);
    }
}
