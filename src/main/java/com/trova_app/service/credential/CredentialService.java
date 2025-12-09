package com.trova_app.service.credential;

import com.trova_app.model.Credential;
import com.trova_app.model.User;
import jakarta.transaction.Transactional;

import javax.crypto.SecretKey;
import java.util.UUID;

public interface CredentialService {

    @Transactional
    void updatePassword(String newPassword, UUID credentialId);

    String encodePassword(String password);

    Credential createAndSaveCredential(String password);
}
