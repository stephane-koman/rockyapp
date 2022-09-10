package com.rockyapp.rockyappbackend.common.pagination;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultPagine<T> implements Serializable {

    private static final long serialVersionUID = 5600040386040333761L;

    private List<T> results;
    private int page;
    private int size;
    private long total;
    private int totalPages;
}
