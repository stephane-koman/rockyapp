package com.rockyapp.rockyappbackend.customers.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchCriteriaDTO implements SocleDTO {
    private String text_search;
    private String name;
    private String email;
    private String mobile;
    private String fixe;
    private int active = 2;
}
