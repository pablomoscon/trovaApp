package com.trovaApp.controller.user;


import com.trovaApp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    }
