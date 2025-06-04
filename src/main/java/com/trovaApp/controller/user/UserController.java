package com.trovaApp.controller.user;

import com.trovaApp.dto.user.UserByIdResponseDTO;
import com.trovaApp.dto.user.UserFindAllResponseDTO;
import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.model.User;
import com.trovaApp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserFindAllResponseDTO>> findAll() {
        List<User> users = userService.findAll();

        List<UserFindAllResponseDTO> userDTOs = users.stream()
                .map(UserFindAllResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserByIdResponseDTO> findByIdWithActivities(@PathVariable UUID userId) {
        User user = userService.findByIdWithActivities(userId);
        return ResponseEntity.ok(new UserByIdResponseDTO(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserByIdResponseDTO> patchUser(
            @PathVariable UUID userId,
            @RequestBody @Valid UserPatchDTO patchDto) {

        User updatedUser = userService.patchUser(userId, patchDto);

        return ResponseEntity.ok(new UserByIdResponseDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> suspendUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok("User suspended successfully");
    }
}
