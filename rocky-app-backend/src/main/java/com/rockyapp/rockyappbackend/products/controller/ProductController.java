package com.rockyapp.rockyappbackend.products.controller;

import com.rockyapp.rockyappbackend.common.dto.StatusDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
import com.rockyapp.rockyappbackend.products.exception.ProductAlreadyExistsException;
import com.rockyapp.rockyappbackend.products.exception.ProductNotFoundException;
import com.rockyapp.rockyappbackend.products.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/product")
@RestController
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping("/search")
    @PostAuthorize("hasAnyAuthority('READ_PRODUCT', 'CREATE_PRODUCT', 'UPDATE_PRODUCT', 'DELETE_PRODUCT')")
    public ResponseEntity<ResultPagine<SimpleProductDTO>> search(@RequestBody(required = false) ProductSearchCriteriaDTO criteriaDTO, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        ResultPagine<SimpleProductDTO> productPage = productService.search(criteriaDTO, pageable);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('READ_PRODUCT', 'CREATE_PRODUCT', 'UPDATE_PRODUCT', 'DELETE_PRODUCT')")
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") String id) throws ProductNotFoundException {
        ProductDTO productDTO = productService.findById(id);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping
    @PostAuthorize("hasAnyAuthority('CREATE_PRODUCT', 'UPDATE_PRODUCT', 'DELETE_PRODUCT')")
    public ResponseEntity<Void> create(@RequestBody @Validated ProductCreaDTO productDto) throws ProductAlreadyExistsException {
        productService.create(productDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_PRODUCT', 'DELETE_PRODUCT')")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody @Validated ProductCreaDTO productDto) throws ProductAlreadyExistsException, ProductNotFoundException {
        productService.update(id, productDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PostAuthorize("hasAnyAuthority('UPDATE_PRODUCT', 'DELETE_PRODUCT')")
    public void updateStatus(@PathVariable(name = "id") String id, @RequestBody StatusDTO statusDTO) throws ProductNotFoundException {
        productService.changeStatus(id, statusDTO.isActive());
    }

    @DeleteMapping("/{id}")
    @PostAuthorize("hasAuthority('DELETE_PRODUCT')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) throws ProductNotFoundException {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }
    
}
