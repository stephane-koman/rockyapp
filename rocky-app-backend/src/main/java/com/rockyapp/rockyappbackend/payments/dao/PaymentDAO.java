package com.rockyapp.rockyappbackend.payments.dao;

import com.rockyapp.rockyappbackend.payments.entity.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentDAO extends PagingAndSortingRepository<Payment, Long> {
}