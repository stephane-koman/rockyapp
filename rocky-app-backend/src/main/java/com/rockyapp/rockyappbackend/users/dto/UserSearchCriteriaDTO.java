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
public class UserSearchCriteriaDTO implements SocleDTO {
    private String text_search;
    private String name;
    private String username;
    private String email;
    private int active = 2;

    private List<String> roleList = new ArrayList<>();
}
