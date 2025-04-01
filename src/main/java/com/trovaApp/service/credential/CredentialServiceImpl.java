package com.trovaApp.service.credential;

import com.trovaApp.model.Credential;
import com.trovaApp.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
public class CredentialServiceImpl implements CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Credential createAndSaveCredential(String password) {
        // Check if the credential already exists with the same ID
        // If the credential exists, return it, else create a new one

        // Create a new instance of Credential
        Credential credential = new Credential();
        credential.setPassword(password); // Assign the plain password

        // Encrypt the password
        credential.setPassword(passwordEncoder.encode(password));

        // Save the credential to the database
        return credentialRepository.save(credential);
    }

    @Transactional
    @Override
    public void updatePassword(String newPassword, UUID credentialId) {
        credentialRepository.findById(credentialId).ifPresent(credential -> {
            credential.setPassword(passwordEncoder.encode(newPassword));
            credentialRepository.save(credential);
        });
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
