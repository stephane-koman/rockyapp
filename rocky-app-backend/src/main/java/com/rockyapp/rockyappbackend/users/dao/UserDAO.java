package com.rockyapp.rockyappbackend.users.dao;

import com.rockyapp.rockyappbackend.users.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDAO extends PagingAndSortingRepository<User, Long> {
    User findUserByUsername(final String username);
}
