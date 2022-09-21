package com.rockyapp.rockyappbackend.customers.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements SocleDTO {
    private String id;
    private String name;
    private String email;
    private String mobile;
    private String fixe;
    private String address;
    private String description;
    private boolean active = true;
}
