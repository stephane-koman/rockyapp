package com.rockyapp.rockyappbackend.users.dao.Impl;

import com.rockyapp.rockyappbackend.users.dao.UserDAOCustom;
import com.rockyapp.rockyappbackend.users.dto.UserSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;

import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.*;

import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAOCustomImpl implements UserDAOCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> searchUsers(UserSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new UserSearchCriteriaDTO();

        Predicate deleteP = createIntegerPredicate(0, user.get("delete"), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getName())) {
            Predicate nameP = createStringPredicate(criteriaDTO.getName(), user.get("name"), cb);
            predicates.add(nameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getUsername())) {
            Predicate usernameP = createStringPredicate(criteriaDTO.getUsername(), user.get("username"), cb);
            predicates.add(usernameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getEmail())) {
            Predicate emailP = createStringPredicate(criteriaDTO.getUsername(), user.get("email"), cb);
            predicates.add(emailP);
        }

        if (Arrays.asList(0, 1).contains(criteriaDTO.getActive())) {
            Predicate activeP = createIntegerPredicate(criteriaDTO.getActive(), user.get("active"), cb);
            predicates.add(activeP);
        }

        Predicate[] finalPredicates = new Predicate[predicates.size()];
        predicates.toArray(finalPredicates);

        cq.select(user)
                .where(finalPredicates)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), user, cb));

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
