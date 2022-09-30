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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/volume")
public class VolumeController {

    private VolumeService volumeService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_VOLUME', 'CREATE_VOLUME', 'UPDATE_VOLUME', 'DELETE_VOLUME')")
    public ResponseEntity<ResultPagine<VolumeDTO>> search(@RequestBody(required = false) VolumeSearchCriteriaDTO criteriaDTO,
                                 Pageable pageable){
        ResultPagine<VolumeDTO> resultPagine = volumeService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(resultPagine);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_VOLUME', 'CREATE_VOLUME', 'UPDATE_VOLUME', 'DELETE_VOLUME')")
    public ResponseEntity<VolumeDTO> findById(@PathVariable(name = "id") Long id) throws VolumeNotFoundException {
        VolumeDTO volumeDTO = volumeService.findById(id);
        return ResponseEntity.ok(volumeDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_VOLUME', 'UPDATE_VOLUME', 'DELETE_VOLUME')")
    public ResponseEntity<Void> create(@RequestBody VolumeDTO volumeDTO) throws VolumeAlreadyExistsException {
        volumeService.create(volumeDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_VOLUME', 'DELETE_VOLUME')")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id, @RequestBody VolumeDTO volumeDTO) throws VolumeNotFoundException, VolumeAlreadyExistsException {
        volumeService.update(id, volumeDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_VOLUME', 'DELETE_VOLUME')")
    public ResponseEntity<Void> updateStatus(@PathVariable(name = "id") Long id, @RequestBody StatusDTO statusDTO) throws VolumeNotFoundException {
        volumeService.changeStatus(id, statusDTO.isActive());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_VOLUME')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) throws VolumeNotFoundException {
        volumeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
