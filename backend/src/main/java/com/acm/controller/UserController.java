package com.acm.controller;

import com.acm.dto.UserResponse;
import com.acm.service.UserService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService users;

    public UserController(UserService users) { this.users = users; }

    @GetMapping
    public List<UserResponse> list() {
        return users.findAll();
    }
}