package com.trovaApp.controller.user;

import com.trovaApp.dto.user.UserByIdResponseDTO;
import com.trovaApp.dto.user.UserFindAllResponseDTO;
import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.dto.user.UserResponseDTO;
import com.trovaApp.model.User;
import com.trovaApp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<UserFindAllResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.findAll(pageable);

        Page<UserFindAllResponseDTO> dtoPage = usersPage.map(UserFindAllResponseDTO::new);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<User> userPage = userService.search(q, page, size);
        Page<UserResponseDTO> dtoPage = userPage.map(UserResponseDTO::new);
        return ResponseEntity.ok(dtoPage);
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

    @PutMapping("/suspend/{id}")
    public ResponseEntity<String> suspendUser(@PathVariable UUID id) {
        userService.suspendUser(id);
        return ResponseEntity.ok("User suspended successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}


