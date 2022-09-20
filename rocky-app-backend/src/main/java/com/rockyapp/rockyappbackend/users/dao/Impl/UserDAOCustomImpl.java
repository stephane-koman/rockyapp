package com.rockyapp.rockyappbackend.users.dao.Impl;

import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.dao.UserDAOCustom;
import com.rockyapp.rockyappbackend.users.dto.UserSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.utils.enums.UserEnum;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createIntegerPredicate;
import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createStringPredicate;

public class UserDAOCustomImpl implements UserDAOCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> searchUsers(UserSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        Join<User, Role> useRoles = user.join("roles", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new UserSearchCriteriaDTO();

        Predicate deleteP = createIntegerPredicate(0, user.get(UserEnum.DELETE.getValue()), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getText_search())) {
            Predicate nameP = createStringPredicate(criteriaDTO.getText_search(), user.get(UserEnum.NAME.getValue()), cb);
            Predicate usernameP = createStringPredicate(criteriaDTO.getText_search(), user.get(UserEnum.USERNAME.getValue()), cb);
            Predicate emailP = createStringPredicate(criteriaDTO.getText_search(), user.get(UserEnum.EMAIL.getValue()), cb);
            Predicate rolesP = cb.like(useRoles.get(UserEnum.NAME.getValue()), "%" + StringHelper.unaccent(criteriaDTO.getText_search().toUpperCase()) + "%");

            Predicate combineP = cb.or(nameP, usernameP, emailP, rolesP);
            predicates.add(combineP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getName())) {
            Predicate nameP = createStringPredicate(criteriaDTO.getName(), user.get(UserEnum.NAME.getValue()), cb);
            predicates.add(nameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getUsername())) {
            Predicate usernameP = createStringPredicate(criteriaDTO.getUsername(), user.get(UserEnum.USERNAME.getValue()), cb);
            predicates.add(usernameP);
        }

        if (StringUtils.isNotEmpty(criteriaDTO.getEmail())) {
            Predicate emailP = createStringPredicate(criteriaDTO.getUsername(), user.get(UserEnum.EMAIL.getValue()), cb);
            predicates.add(emailP);
        }

        if (Arrays.asList(0, 1).contains(criteriaDTO.getActive())) {
            Predicate activeP = createIntegerPredicate(criteriaDTO.getActive(), user.get(UserEnum.ACTIVE.getValue()), cb);
            predicates.add(activeP);
        }

        if(criteriaDTO.getRoleList() != null && !criteriaDTO.getRoleList().isEmpty()){
            CriteriaBuilder.In<String> inRoles = cb.in(useRoles.get(UserEnum.NAME.getValue()));
            criteriaDTO.getRoleList().forEach(inRoles::value);
            predicates.add(inRoles);
        }

        Predicate[] finalPredicates = new Predicate[predicates.size()];
        predicates.toArray(finalPredicates);

        cq.select(user)
                .where(finalPredicates)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), user, cb))
                .groupBy(user.get(UserEnum.ID.getValue()));

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
