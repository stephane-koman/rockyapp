package com.rockyapp.rockyappbackend.products.dao.impl;

import com.rockyapp.rockyappbackend.common.dao.SocleDAO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import com.rockyapp.rockyappbackend.products.dao.ProductDAOCustom;
import com.rockyapp.rockyappbackend.products.dto.ProductSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.utils.enums.ProductEnum;
import com.rockyapp.rockyappbackend.utils.helpers.ArrayHelper;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createPredicate;

@Component
public class ProductDAOCustomImpl extends SocleDAO implements ProductDAOCustom {
    @Override
    public Page<Product> search(ProductSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> productRoot = cq.from(Product.class);
        Join<Product, ProductType> productType = productRoot.join("productType", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new ProductSearchCriteriaDTO();

        Predicate deleteP = createPredicate(0, productType.get(ProductEnum.DELETE.getValue()), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getText_search())) {
            Predicate nameP = DaoHelper.createPredicate(criteriaDTO.getText_search(), productRoot.get(ProductEnum.NAME.getValue()), cb);
            Predicate priceP = DaoHelper.createPredicate(criteriaDTO.getText_search(), productRoot.get(ProductEnum.PRICE.getValue()), cb);
            Predicate productTypeP = cb.like(productType.get(ProductEnum.NAME.getValue()), "%" + StringHelper.unaccent(criteriaDTO.getText_search().toUpperCase()) + "%");

            Predicate combineP = cb.or(nameP, priceP, productTypeP);
            predicates.add(combineP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getName())) {
            Predicate nameP = DaoHelper.createPredicate(criteriaDTO.getName(), productRoot.get(ProductEnum.NAME.getValue()), cb);
            predicates.add(nameP);
        }

        if (criteriaDTO.getPrice() != null) {
            Predicate usernameP = createPredicate(criteriaDTO.getPrice(), productRoot.get(ProductEnum.PRICE.getValue()), cb);
            predicates.add(usernameP);
        }

        if (ArrayHelper.verifyIntIsBoolean(criteriaDTO.getActive())) {
            Predicate activeP = createPredicate(criteriaDTO.getActive(), productRoot.get(ProductEnum.ACTIVE.getValue()), cb);
            predicates.add(activeP);
        }

        if(criteriaDTO.getProductTypes() != null && !criteriaDTO.getProductTypes().isEmpty()){
            CriteriaBuilder.In<String> inProductTypes = cb.in(productType.get(ProductEnum.NAME.getValue()));
            criteriaDTO.getProductTypes().forEach(inProductTypes::value);
            predicates.add(inProductTypes);
        }

        Predicate[] finalPredicates = new Predicate[predicates.size()];
        predicates.toArray(finalPredicates);

        cq.select(productRoot)
                .where(finalPredicates)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), productRoot, cb))
                .groupBy(productRoot.get(ProductEnum.ID.getValue()));

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
