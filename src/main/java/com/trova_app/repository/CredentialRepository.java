package com.trova_app.repository;

import com.trova_app.model.Credential;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, UUID> {

        @Transactional
        @Modifying
        @Query("UPDATE Credential c SET c.password = :password WHERE c.id = :credentialId")
        void updatePassword(UUID credentialId, String password);
}
