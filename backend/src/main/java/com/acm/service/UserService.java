package com.acm.service;

import com.acm.dto.UserResponse;
import com.acm.entity.Role;
import com.acm.entity.User;
import com.acm.exception.ApiException;
import com.acm.repository.RefreshTokenRepository;
import com.acm.repository.UserRepository;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;

    public UserService(UserRepository users, RefreshTokenRepository refreshTokens) {
        this.users = users;
        this.refreshTokens = refreshTokens;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return users.findAll(Sort.by(Sort.Order.asc("nom").ignoreCase(), Sort.Order.asc("prenom").ignoreCase()))
                .stream().map(UserResponse::from).toList();
    }

    /**
     * The acting admin can never remove his own ADMIN role or delete himself.
     * Since the actor is an admin, at least one admin always remains.
     */
    @Transactional
    public UserResponse updateRoles(Long id, Set<Role> roles, Long actorId) {
        User target = find(id);
        if (id.equals(actorId) && !roles.contains(Role.ADMIN)) {
            throw new ApiException(HttpStatus.CONFLICT, "Vous ne pouvez pas retirer votre propre rôle administrateur.");
        }
        target.setRoles(roles);
        return UserResponse.from(target);
    }

    /** Deletes the user, his roles (user_roles) and all his refresh tokens, explicitly and not only via the DB. */
    @Transactional
    public void delete(Long id, Long actorId) {
        if (id.equals(actorId)) {
            throw new ApiException(HttpStatus.CONFLICT, "Vous ne pouvez pas supprimer votre propre compte.");
        }
        find(id);
        refreshTokens.deleteByUserId(id);
        users.deleteById(id);
    }

    private User find(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur introuvable."));
    }
}