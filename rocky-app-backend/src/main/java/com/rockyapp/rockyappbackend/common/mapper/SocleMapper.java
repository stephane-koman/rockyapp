package com.rockyapp.rockyappbackend.common.mapper;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.common.entity.SocleEntity;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import com.rockyapp.rockyappbackend.users.exception.PasswordEmptyException;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SocleMapper <E extends SocleEntity, M extends SocleDTO>{

    E mapToEntity(M model, E entity) throws NotFoundException, PasswordNotMatchException, PasswordEmptyException;
    M mapFromEntity(E entity);
    List<M> mapFromEntity(final List<E> entityList);
    ResultPagine<M> mapFromEntity(final ResultPagine<E> resultPagine);
    ResultPagine<M> mapFromEntity(final Page<E> page);
}
