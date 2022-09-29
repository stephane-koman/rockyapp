package com.rockyapp.rockyappbackend.product_types.dao;

import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeDAO extends PagingAndSortingRepository<ProductType, Long>, ProductTypeDAOCustom {
    @Query("SELECT p FROM ProductType p " +
            "WHERE (LOWER(p.name) = LOWER(:name)) AND p.delete = 0")
    ProductType findProductTypeByNameAndIsNotDelete(String name);

    @Query("SELECT p FROM ProductType p " +
            "WHERE p.id = :id AND p.delete = 0")
    ProductType findProductTypeByIdAndIsNotDelete(@Param("id") Long id);
}