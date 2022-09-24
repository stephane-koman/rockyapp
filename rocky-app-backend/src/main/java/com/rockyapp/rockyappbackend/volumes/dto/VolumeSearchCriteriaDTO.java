package com.rockyapp.rockyappbackend.volumes.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolumeSearchCriteriaDTO implements SocleDTO {
    private String text_search = null;
    private Long quantity = null;
    private MesureEnum mesure = null;
    private int active = 2;
}
