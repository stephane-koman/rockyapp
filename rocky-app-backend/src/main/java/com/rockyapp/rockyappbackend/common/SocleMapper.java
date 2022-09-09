package com.rockyapp.rockyappbackend.common;

import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SocleMapper <E extends SocleEntity, M extends SocleDTO>{

    E mapToEntity(M model, E entity) throws NotFoundException;
    M mapFromEntity(E entity);
    List<M> mapFromEntity(final List<E> entityList);
    ResultPagine<M> mapFromEntity(final ResultPagine<E> resultPagine);
    ResultPagine<M> mapFromEntity(final Page<E> page);
}
