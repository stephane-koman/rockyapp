package com.rockyapp.rockyappbackend.common.dao;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public abstract class SocleDAO {
    @PersistenceContext
    public EntityManager entityManager;
}
