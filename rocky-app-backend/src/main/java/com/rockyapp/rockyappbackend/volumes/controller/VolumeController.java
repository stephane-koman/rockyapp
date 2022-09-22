package com.rockyapp.rockyappbackend.volumes.controller;

import com.rockyapp.rockyappbackend.common.dto.StatusDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeAlreadyExistsException;
import com.rockyapp.rockyappbackend.volumes.exception.VolumeNotFoundException;
import com.rockyapp.rockyappbackend.volumes.service.VolumeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/volume")
public class VolumeController {

    private VolumeService volumeService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_VOLUME', 'CREATE_VOLUME', 'UPDATE_VOLUME', 'DELETE_VOLUME')")
    public ResultPagine<VolumeDTO> searchVolumes(@RequestBody(required = false) VolumeSearchCriteriaDTO criteriaDTO,
                                                               Pageable pageable){
        return volumeService.searchVolumes(criteriaDTO, pageable);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_VOLUME', 'CREATE_VOLUME', 'UPDATE_VOLUME', 'DELETE_VOLUME')")
    public VolumeDTO findVolumeById(@PathVariable(name = "id") Long id) throws VolumeNotFoundException {
        return volumeService.findVolumeById(id);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_VOLUME', 'UPDATE_VOLUME', 'DELETE_VOLUME')")
    public VolumeDTO createVolume(@RequestBody VolumeDTO volumeDTO) throws VolumeAlreadyExistsException {
        return volumeService.create(volumeDTO);
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_VOLUME', 'DELETE_VOLUME')")
    public VolumeDTO updateVolume(@PathVariable(name = "id") Long id, @RequestBody VolumeDTO volumeDTO) throws VolumeNotFoundException, VolumeAlreadyExistsException {
        return volumeService.update(id, volumeDTO);
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_VOLUME', 'DELETE_VOLUME')")
    public void updateUserStatus(@PathVariable(name = "id") Long id, @RequestBody StatusDTO statusDTO) throws VolumeNotFoundException {
        volumeService.changeVolumeStatus(id, statusDTO.isActive());
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_VOLUME')")
    public void deleteVolume(@PathVariable(name = "id") Long id) throws VolumeNotFoundException {
        volumeService.delete(id);
    }
}
