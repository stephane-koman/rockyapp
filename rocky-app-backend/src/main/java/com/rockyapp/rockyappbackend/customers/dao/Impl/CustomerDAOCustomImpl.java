package com.rockyapp.rockyappbackend.customers.dao.Impl;

import com.rockyapp.rockyappbackend.customers.dao.CustomerDAOCustom;
import com.rockyapp.rockyappbackend.customers.dto.CustomerSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.customers.entity.Customer;
import com.rockyapp.rockyappbackend.utils.enums.CustomerEnum;
import com.rockyapp.rockyappbackend.utils.helpers.ArrayHelper;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createIntegerPredicate;
import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createStringPredicate;

public class CustomerDAOCustomImpl implements CustomerDAOCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Customer> search(CustomerSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> customerRoot = cq.from(Customer.class);

        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new CustomerSearchCriteriaDTO();

        Predicate deleteP = createIntegerPredicate(0, customerRoot.get(CustomerEnum.DELETE.getValue()), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getText_search())) {
            Predicate nameP = createStringPredicate(criteriaDTO.getText_search(), customerRoot.get(CustomerEnum.NAME.getValue()), cb);
            Predicate fixeP = createStringPredicate(criteriaDTO.getText_search(), customerRoot.get(CustomerEnum.FIXE.getValue()), cb);
            Predicate mobileP = createStringPredicate(criteriaDTO.getText_search(), customerRoot.get(CustomerEnum.MOBILE.getValue()), cb);
            Predicate emailP = createStringPredicate(criteriaDTO.getText_search(), customerRoot.get(CustomerEnum.EMAIL.getValue()), cb);

            Predicate combineP = cb.or(nameP, mobileP, fixeP, emailP);
            predicates.add(combineP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getName())) {
            Predicate nameP = createStringPredicate(criteriaDTO.getName(), customerRoot.get(CustomerEnum.NAME.getValue()), cb);
            predicates.add(nameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getEmail())) {
            Predicate emailP = createStringPredicate(criteriaDTO.getEmail(), customerRoot.get(CustomerEnum.EMAIL.getValue()), cb);
            predicates.add(emailP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getFixe())) {
            Predicate fixeP = createStringPredicate(criteriaDTO.getFixe(), customerRoot.get(CustomerEnum.FIXE.getValue()), cb);
            predicates.add(fixeP);
        }
        if (StringUtils.isNotEmpty(criteriaDTO.getMobile())) {
            Predicate mobileP = createStringPredicate(criteriaDTO.getMobile(), customerRoot.get(CustomerEnum.MOBILE.getValue()), cb);
            predicates.add(mobileP);
        }

        if (ArrayHelper.verifyIntIsBoolean(criteriaDTO.getActive())) {
            Predicate activeP = createIntegerPredicate(criteriaDTO.getActive(), customerRoot.get(CustomerEnum.ACTIVE.getValue()), cb);
            predicates.add(activeP);
        }

        Predicate[] finalPredicates = new Predicate[predicates.size()];
        predicates.toArray(finalPredicates);

        cq.select(customerRoot)
                .where(finalPredicates)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), customerRoot, cb))
                .groupBy(customerRoot.get(CustomerEnum.ID.getValue()));

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
