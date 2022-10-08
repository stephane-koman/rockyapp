package com.rockyapp.rockyappbackend.orders.dao;

import com.rockyapp.rockyappbackend.orders.entity.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderDAO extends PagingAndSortingRepository<Order, String> {
}