package com.rockyapp.rockyappbackend.users.dao;

import com.rockyapp.rockyappbackend.users.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends PagingAndSortingRepository<User, Long>, UserDAOCustom {

    @Query("SELECT u FROM User u " +
            "WHERE ((LOWER(u.username) = LOWER(:name)) " +
            "OR (LOWER(u.email) = LOWER(:name)))" +
            "AND u.active = 1 AND u.delete = 0")
    User findUserByUsernameOrEmailAndIsActiveAndIsNotDelete(final String name);

    @Query("SELECT u FROM User u " +
            "WHERE u.id = :id AND u.delete = 0")
    User findUserByIdAndIsNotDelete(@Param("id") Long id);

}
