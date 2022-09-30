package com.rockyapp.rockyappbackend.volumes.builder;

import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;

public class VolumeBuilder {

    public static VolumeDTO getDto() {
        VolumeDTO dto = new VolumeDTO();
        dto.setId(1L);
        dto.setQuantity(100L);
        dto.setMesure(MesureEnum.ML);
        return dto;
    }

    public static Volume getEntity() {
        Volume entity = new Volume();
        entity.setId(1L);
        entity.setQuantity(100L);
        entity.setMesure(MesureEnum.ML.name());
        return entity;
    }
}