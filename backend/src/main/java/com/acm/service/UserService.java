package com.acm.service;

import com.acm.dto.UserResponse;
import com.acm.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;

    public UserService(UserRepository users) { this.users = users; }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }
}