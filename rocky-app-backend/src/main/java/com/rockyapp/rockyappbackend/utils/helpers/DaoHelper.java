package com.rockyapp.rockyappbackend.utils.helpers;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.utils.enums.DefaultEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DaoHelper {

    public static Predicate createPredicate(String property, Path<String> propertyPath, CriteriaBuilder cb) {
        return cb.like(cb.function("unaccent", String.class, cb.lower(propertyPath)) , '%' + StringHelper.unaccent(property.toLowerCase()) + '%');
    }

    public static Predicate createPredicate(int property, Path<String> propertyPath, CriteriaBuilder cb) {
        return cb.like(propertyPath.as(String.class), '%' + String.valueOf(property) + '%');
    }

    public static Predicate createPredicate(BigDecimal property, Path<String> propertyPath, CriteriaBuilder cb) {
        return cb.like(propertyPath.as(String.class), '%' + String.valueOf(property) + '%');
    }

    public static <T> void generateDefaultQuery(Root<T> root, CriteriaBuilder cb, CriteriaQuery<T> cq, DefaultCriteriaDTO criteriaDTO, Pageable pageable){
        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new DefaultCriteriaDTO();

        Predicate deleteP = createPredicate(0, root.get(DefaultEnum.DELETE.getValue()), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getText_search())) {
            Predicate nameP = createPredicate(criteriaDTO.getText_search(), root.get(DefaultEnum.NAME.getValue()), cb);
            Predicate descriptionP = createPredicate(criteriaDTO.getText_search(), root.get(DefaultEnum.DESCRIPTION.getValue()), cb);
            Predicate combineP = cb.or(nameP, descriptionP);
            predicates.add(combineP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getName())) {
            Predicate nameP = createPredicate(criteriaDTO.getName(), root.get(DefaultEnum.NAME.getValue()), cb);
            predicates.add(nameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getDescription())) {
            Predicate descriptionP = createPredicate(criteriaDTO.getDescription(), root.get(DefaultEnum.DESCRIPTION.getValue()), cb);
            predicates.add(descriptionP);
        }

        if (ArrayHelper.verifyIntIsBoolean(criteriaDTO.getActive())) {
            Predicate activeP = createPredicate(criteriaDTO.getActive(), root.get(DefaultEnum.ACTIVE.getValue()), cb);
            predicates.add(activeP);
        }

        Predicate[] finalPredicates = new Predicate[predicates.size()];
        predicates.toArray(finalPredicates);

        cq.select(root)
                .where(finalPredicates)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
    }

    public static <T> Page<T> returnResults (EntityManager entityManager, CriteriaQuery<T> cq, Pageable pageable){
        TypedQuery<T> query = entityManager.createQuery(cq);
        int totalRows = query.getResultList().size();
        Pageable page = null;
        if(pageable.getPageSize() > 1){
            page = pageable;
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }else{
            page = PageRequest.of(0, Integer.MAX_VALUE);
        }

        return new PageImpl<T>(query.getResultList(), page, totalRows);
    }
}
