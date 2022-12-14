package com.rockyapp.rockyappbackend.utils.helpers;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DaoHelper {

    public static Predicate createStringPredicate(String property, Path<String> propertyPath, CriteriaBuilder cb) {
        return cb.like(cb.function("unaccent", String.class, cb.lower(propertyPath)) , '%' + StringHelper.unaccent(property.toLowerCase()) + '%');
    }

    public static Predicate createIntegerPredicate(int property, Path<String> propertyPath, CriteriaBuilder cb) {
        return cb.equal(propertyPath, property);
    }

    public static <T> void generateDefaultQuery(Root<T> root, CriteriaBuilder cb, CriteriaQuery<T> cq, DefaultCriteriaDTO criteriaDTO, Pageable pageable){
        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new DefaultCriteriaDTO();

        Predicate deleteP = createIntegerPredicate(0, root.get("delete"), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getName())) {
            Predicate nameP = createStringPredicate(criteriaDTO.getName(), root.get("name"), cb);
            predicates.add(nameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getDescription())) {
            Predicate usernameP = createStringPredicate(criteriaDTO.getDescription(), root.get("description"), cb);
            predicates.add(usernameP);
        }

        if (ArrayHelper.verifyIntIsBoolean(criteriaDTO.getActive())) {
            Predicate activeP = createIntegerPredicate(criteriaDTO.getActive(), root.get("active"), cb);
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
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<T>(query.getResultList(), pageable, totalRows);
    }
}
