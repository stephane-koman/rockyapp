package com.rockyapp.rockyappbackend.volumes.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeAlreadyExistsException;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeNotFoundException;
import org.springframework.data.domain.Pageable;

public interface VolumeService {
    ResultPagine<VolumeDTO> searchVolumes(final VolumeSearchCriteriaDTO criteriaDTO, final Pageable pageable);
    VolumeDTO findVolumeById(final Long id) throws VolumeNotFoundException;
    VolumeDTO create(VolumeDTO volumeDTO) throws VolumeAlreadyExistsException;
    VolumeDTO update(Long volumeId, VolumeDTO volumeDTO) throws VolumeAlreadyExistsException, VolumeNotFoundException;
    void delete(Long volumeId) throws VolumeNotFoundException;

    void changeVolumeStatus(Long id, boolean active) throws VolumeNotFoundException;
}
