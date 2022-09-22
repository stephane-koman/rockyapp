package com.rockyapp.rockyappbackend.volumes.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VolumeMapper extends AbstractSocleMapper<Volume, VolumeDTO> implements SocleMapper<Volume, VolumeDTO> {
    @Override
    public Volume mapToEntity(VolumeDTO model, Volume entity) {
        BeanUtils.copyProperties(model, entity, "id", "active");
        entity.setActive(model.isActive() ? 1 : 0);
        return entity;
    }

    @Override
    public VolumeDTO mapFromEntity(Volume entity) {
        VolumeDTO volumeDTO = new VolumeDTO();
        BeanUtils.copyProperties(entity, volumeDTO, "active");
        volumeDTO.setActive(entity.getActive() == 1);
        return volumeDTO;
    }
}
