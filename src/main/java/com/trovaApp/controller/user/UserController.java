package com.trovaApp.controller.user;

import com.trovaApp.dto.user.UserPatchDTO;
import com.trovaApp.model.User;
import com.trovaApp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<User> patchUser(
            @PathVariable UUID userId,
            @RequestBody @Valid UserPatchDTO patchDto) {

        User updatedUser = userService.patchUser(userId, patchDto);

        return ResponseEntity.ok(updatedUser);
    }
}
