package com.rockyapp.rockyappbackend.roles.dao.impl;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAOCustom;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RoleDAOCustomImpl implements RoleDAOCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Role> searchRoles(DefaultCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> roleRoot = cq.from(Role.class);

        DaoHelper.generateDefaultQuery(roleRoot, cb, cq, criteriaDTO, pageable);

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
