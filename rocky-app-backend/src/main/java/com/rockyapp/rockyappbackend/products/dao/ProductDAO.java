package com.rockyapp.rockyappbackend.products.dao;

import com.rockyapp.rockyappbackend.products.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface ProductDAO extends PagingAndSortingRepository<Product, String>, ProductDAOCustom {
    @Query("SELECT p FROM Product p " +
            "WHERE (LOWER(p.name) = LOWER(:name)) " +
            "AND p.productType.id = :productTypeId " +
            "AND p.volume.id = :volumeId " +
            "AND p.delete = 0")
    Product findProductByNameAndProductTypeAndVolumeAndIsNotDelete(@Param("name") String name, @Param("productTypeId") Long productTypeId, @Param("volumeId") Long volumeId);

    @Query("SELECT p FROM Product p " +
            "WHERE p.id = :id AND p.delete = 0")
    Product findProductByIdAndIsNotDelete(@Param("id") String id);
}