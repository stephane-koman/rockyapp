package com.rockyapp.rockyappbackend.permissions.dao.impl;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.permissions.dao.PermissionDAOCustom;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PermissionDAOCustomImpl implements PermissionDAOCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Permission> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> permissionRoot = cq.from(Permission.class);

        DaoHelper.generateDefaultQuery(permissionRoot, cb, cq, criteriaDTO, pageable);

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
