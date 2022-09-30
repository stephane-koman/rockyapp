package com.rockyapp.rockyappbackend.volumes.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.volumes.dao.VolumeDAO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeAlreadyExistsException;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeNotFoundException;
import com.rockyapp.rockyappbackend.volumes.mapper.VolumeMapper;
import com.rockyapp.rockyappbackend.volumes.service.VolumeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class VolumeServiceImpl implements VolumeService {

    private VolumeDAO volumeDAO;
    private VolumeMapper  volumeMapper;

    @Override
    public ResultPagine<VolumeDTO> search(VolumeSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Volume> volumePage = volumeDAO.search(criteriaDTO, pageable);
        return volumeMapper.mapFromEntity(volumePage);
    }

    private Volume findVolumeById(Long id) throws VolumeNotFoundException {
        Volume volume = volumeDAO.findVolumeByIdAndIsNotDelete(id);

        if(volume == null) throw new VolumeNotFoundException();

        return volume;
    }

    @Override
    public VolumeDTO findById(Long id) throws VolumeNotFoundException {
        Volume volume = this.findVolumeById(id);
        return volumeMapper.mapFromEntity(volume);
    }

    @Override
    public void create(VolumeDTO volumeDTO) throws VolumeAlreadyExistsException {
        Volume volumeExists = volumeDAO.findVolumeByQuantityAndMesureAndIsNotDelete(volumeDTO.getQuantity(), volumeDTO.getMesure().name());

        if(volumeExists != null) throw new VolumeAlreadyExistsException(volumeDTO.getQuantity().toString().concat(" " + volumeDTO.getMesure()));

        Volume volume = new Volume();
        volume = volumeMapper.mapToEntity(volumeDTO, volume);
        volumeDAO.save(volume);
    }

    @Override
    public void update(Long volumeId, VolumeDTO volumeDTO) throws VolumeAlreadyExistsException, VolumeNotFoundException {
        Volume volume  = volumeDAO.findVolumeByQuantityAndMesureAndIsNotDelete(volumeDTO.getQuantity(), volumeDTO.getMesure().name());
        if(volume != null && !volume.getId().equals(volumeId)) throw new VolumeAlreadyExistsException(volumeDTO.getQuantity().toString().concat(" " + volumeDTO.getMesure()));

        volume = this.findVolumeById(volumeId);
        volume = volumeMapper.mapToEntity(volumeDTO, volume);
        volume.setUpdatedAt(LocalDateTime.now());
        volumeDAO.save(volume);
    }

    @Override
    public void delete(Long volumeId) throws VolumeNotFoundException {
        Volume volume = volumeDAO.findById(volumeId).orElseThrow(VolumeNotFoundException::new);
        volume.setDelete(1);
        volume.setActive(0);
        volume.setUpdatedAt(LocalDateTime.now());
        volume.setDeletedAt(LocalDateTime.now());
        volumeDAO.save(volume);
    }

    @Override
    public void changeStatus(Long id, boolean active) throws VolumeNotFoundException {
        Volume volume = volumeDAO.findById(id).orElseThrow(VolumeNotFoundException::new);
        volume.setActive(Boolean.TRUE.equals(active) ? 1 : 0);
        volume.setUpdatedAt(LocalDateTime.now());
        volumeDAO.save(volume);
    }
}
