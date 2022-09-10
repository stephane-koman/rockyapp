package com.rockyapp.rockyappbackend.common.mapper;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.common.entity.SocleEntity;
import com.rockyapp.rockyappbackend.exceptions.NotFoundException;
import com.rockyapp.rockyappbackend.users.exception.PasswordNotMatchException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSocleMapper<E extends SocleEntity, M extends SocleDTO> implements SocleMapper<E, M> {

    @Override
    public abstract E mapToEntity(final M model, final E entity) throws NotFoundException, PasswordNotMatchException;

    @Override
    public abstract M mapFromEntity(final E entity);

    @Override
    public List<M> mapFromEntity(final List<E> entityList) {
        return entityList.stream().map(this::mapFromEntity).collect(Collectors.toList());
    }

    @Override
    public ResultPagine<M> mapFromEntity(ResultPagine<E> resultPagine) {

        return ResultPagine.<M>builder()
                .page(resultPagine.getPage())
                .size(resultPagine.getSize())
                .total(resultPagine.getTotal())
                .totalPages(resultPagine.getTotalPages())
                .results(mapFromEntity(resultPagine.getResults()))
                .build();
    }

    @Override
    public ResultPagine<M> mapFromEntity(Page<E> pageResult) {
        return ResultPagine.<M>builder()
                .page(pageResult.getNumber())
                .size(pageResult.getNumberOfElements())
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .results(mapFromEntity(pageResult.getContent()))
                .build();
    }
}
