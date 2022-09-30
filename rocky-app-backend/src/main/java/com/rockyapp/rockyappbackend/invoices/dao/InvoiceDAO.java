package com.rockyapp.rockyappbackend.invoices.dao;

import com.rockyapp.rockyappbackend.invoices.entity.Invoice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceDAO extends PagingAndSortingRepository<Invoice, String> {
}