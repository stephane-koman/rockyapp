package com.rockyapp.rockyappbackend.products.dao;

import com.rockyapp.rockyappbackend.products.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface ProductDAO extends PagingAndSortingRepository<Product, String> {
}