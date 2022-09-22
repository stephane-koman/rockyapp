package com.rockyapp.rockyappbackend.volumes.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolumeDTO implements SocleDTO {
    private Long id;
    private Long quantity;
    private MesureEnum mesure;
    private String description;
    private boolean active;
}
