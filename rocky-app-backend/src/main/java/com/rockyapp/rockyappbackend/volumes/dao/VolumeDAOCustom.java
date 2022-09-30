package com.rockyapp.rockyappbackend.volumes.dao;

import com.rockyapp.rockyappbackend.volumes.dto.VolumeSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VolumeDAOCustom {
    Page<Volume> search(VolumeSearchCriteriaDTO criteriaDTO, Pageable pageable);
}
