package com.rockyapp.rockyappbackend.users.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements SocleDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String passwordConfirm;

    private int isActive;

    private List<String> roleList = new ArrayList<>();
    private List<String> permissionList = new ArrayList<>();
}
