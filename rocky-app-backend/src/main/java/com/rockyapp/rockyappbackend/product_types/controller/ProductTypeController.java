package com.rockyapp.rockyappbackend.product_types.controller;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.exception.ProductTypeAlreadyExistsException;
import com.rockyapp.rockyappbackend.product_types.exception.ProductTypeNotFoundException;
import com.rockyapp.rockyappbackend.product_types.service.ProductTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/product-type")
@RestController
@AllArgsConstructor
public class ProductTypeController {

    private ProductTypeService productTypeService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_PRODUCT_TYPE', 'CREATE_PRODUCT_TYPE', 'UPDATE_PRODUCT_TYPE', 'DELETE_PRODUCT_TYPE')")
    public ResponseEntity<ResultPagine<ProductTypeDTO>> search(@RequestBody(required = false) DefaultCriteriaDTO criteriaDTO, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        ResultPagine<ProductTypeDTO> productTypePage = productTypeService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(productTypePage);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_PRODUCT_TYPE', 'CREATE_PRODUCT_TYPE', 'UPDATE_PRODUCT_TYPE', 'DELETE_PRODUCT_TYPE')")
    public ResponseEntity<ProductTypeDTO> findById(@PathVariable("id") Long id) throws ProductTypeNotFoundException {
        ProductTypeDTO productTypeDTO = productTypeService.findById(id);
        return ResponseEntity.ok(productTypeDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_PRODUCT_TYPE', 'UPDATE_PRODUCT_TYPE', 'DELETE_PRODUCT_TYPE')")
    public ResponseEntity<Void> create(@RequestBody @Validated ProductTypeDTO productTypeDto) throws ProductTypeAlreadyExistsException {
        productTypeService.create(productTypeDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_PRODUCT_TYPE', 'DELETE_PRODUCT_TYPE')")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody @Validated ProductTypeDTO productTypeDto) throws ProductTypeAlreadyExistsException, ProductTypeNotFoundException {
        productTypeService.update(id, productTypeDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_PRODUCT_TYPE')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ProductTypeNotFoundException {
        productTypeService.delete(id);
        return ResponseEntity.ok().build();
    }
}