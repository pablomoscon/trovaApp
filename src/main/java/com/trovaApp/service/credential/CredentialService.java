package com.trovaApp.service.credential;

import com.trovaApp.model.Credential;
import com.trovaApp.model.User;
import jakarta.transaction.Transactional;

import javax.crypto.SecretKey;
import java.util.UUID;

public interface CredentialService {

    @Transactional
    void updatePassword(String newPassword, UUID credentialId);

    String encodePassword(String password);

    Credential createAndSaveCredential(String password);
}
