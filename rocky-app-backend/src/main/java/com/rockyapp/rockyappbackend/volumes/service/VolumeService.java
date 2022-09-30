package com.rockyapp.rockyappbackend.volumes.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeAlreadyExistsException;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeNotFoundException;
import org.springframework.data.domain.Pageable;

public interface VolumeService {
    ResultPagine<VolumeDTO> search(final VolumeSearchCriteriaDTO criteriaDTO, final Pageable pageable);
    VolumeDTO findById(final Long id) throws VolumeNotFoundException;
    void create(VolumeDTO volumeDTO) throws VolumeAlreadyExistsException;
    void update(Long volumeId, VolumeDTO volumeDTO) throws VolumeAlreadyExistsException, VolumeNotFoundException;
    void delete(Long volumeId) throws VolumeNotFoundException;

    void changeStatus(Long id, boolean active) throws VolumeNotFoundException;
}
