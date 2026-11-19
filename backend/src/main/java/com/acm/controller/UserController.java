package com.acm.controller;

import com.acm.dto.RolesRequest;
import com.acm.dto.UserResponse;
import com.acm.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/roles")
    public UserResponse updateRoles(@PathVariable Long id, @Valid @RequestBody RolesRequest req,
                                    @AuthenticationPrincipal Jwt jwt) {
        return users.updateRoles(id, Set.copyOf(req.roles()), Long.valueOf(jwt.getSubject()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        users.delete(id, Long.valueOf(jwt.getSubject()));
    }
}