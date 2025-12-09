package com.trova_app.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trova_app.dto.user.UserSeedDTO;
import com.trova_app.dto.user.UserSignupDTO;
import com.trova_app.service.user.UserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class UserSeeder {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    public UserSeeder(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    public void seed() throws Exception {

        // Evitar sembrar si ya hay usuarios
        if (userService.getTotalUsers() > 1) return;

        ClassPathResource resource = new ClassPathResource("seed/users.json");
        try (InputStream is = resource.getInputStream()) {

            List<UserSeedDTO> seeds = objectMapper.readValue(
                    is, new TypeReference<List<UserSeedDTO>>() {});

            for (UserSeedDTO seed : seeds) {

                if (userService.findByUsername(seed.getUsername()).isPresent()) {
                    continue;
                }

                UserSignupDTO dto = new UserSignupDTO();
                dto.setUsername(seed.getUsername());
                dto.setEmail(seed.getEmail());
                dto.setName(seed.getName());
                dto.setPassword(seed.getPassword());
                dto.setConfirmPassword(seed.getPassword());
                dto.setRole(seed.getRole());

                userService.save(dto);
                System.out.printf("Seeded user: %s%n", dto.getUsername());
            }
        }
    }
}
