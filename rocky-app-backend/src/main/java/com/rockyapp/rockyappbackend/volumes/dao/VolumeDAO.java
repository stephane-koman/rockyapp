package com.rockyapp.rockyappbackend.volumes.dao;

import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface VolumeDAO extends PagingAndSortingRepository<Volume, Long>, VolumeDAOCustom {

    @Query("SELECT v FROM Volume v " +
            "WHERE v.quantity = :quantity AND v.mesure = :mesure AND v.delete = 0")
    Volume findVolumeByQuantityAndMesureAndIsNotDelete(@Param("quantity") Long quantity, @Param("mesure") MesureEnum mesure);

    @Query("SELECT v FROM Volume v " +
            "WHERE v.id = :id AND v.delete = 0")
    Volume findVolumeByIdAndIsNotDelete(@Param("id") Long id);
}
