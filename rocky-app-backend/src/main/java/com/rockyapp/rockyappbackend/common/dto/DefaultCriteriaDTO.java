package com.rockyapp.rockyappbackend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultCriteriaDTO implements SocleDTO {
    private String name;
    private String description;
    private int active = 2;
}
