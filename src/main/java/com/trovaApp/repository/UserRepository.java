package com.trovaApp.repository;

import com.trovaApp.enums.Role;
import com.trovaApp.enums.Status;
import com.trovaApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"activities"})
    Optional<User> findWithActivitiesById(UUID id);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.username = :username")
    void updateUserRole(@Param("username") String username, @Param("role") Role role);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.credential WHERE u.username = :username")
    Optional<User> findByUsernameWithCredentials(@Param("username") String username);

    @EntityGraph(attributePaths = {"activities"})
    Page<User> findAllByStatusNot(Status status, Pageable pageable);

    @Query("""
        SELECT u
        FROM User u
        WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))
           OR LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%'))
    """)
    Page<User> searchUsers(@Param("term") String term, Pageable pageable);

    long countByStatus(Status status);
}


