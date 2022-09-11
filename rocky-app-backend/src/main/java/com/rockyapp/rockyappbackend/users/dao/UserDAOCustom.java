package com.rockyapp.rockyappbackend.users.dao;

import com.rockyapp.rockyappbackend.users.dto.UserSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDAOCustom {
    Page<User> searchUsers(UserSearchCriteriaDTO criteriaDTO, Pageable pageable);
}
