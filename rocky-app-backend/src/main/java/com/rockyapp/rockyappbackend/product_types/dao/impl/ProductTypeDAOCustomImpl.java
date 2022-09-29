package com.rockyapp.rockyappbackend.product_types.dao.impl;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.product_types.dao.ProductTypeDAOCustom;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ProductTypeDAOCustomImpl implements ProductTypeDAOCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductType> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductType> cq = cb.createQuery(ProductType.class);
        Root<ProductType> productTypeRoot = cq.from(ProductType.class);

        DaoHelper.generateDefaultQuery(productTypeRoot, cb, cq, criteriaDTO, pageable);

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
