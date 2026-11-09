package com.acm.config;

import com.acm.entity.Role;
import com.acm.entity.User;
import com.acm.repository.UserRepository;
import com.acm.security.PasswordPolicy;
import java.util.List;
import java.util.Locale;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final AdminProperties props;
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final PasswordPolicy policy;

    public AdminInitializer(AdminProperties props, UserRepository users, PasswordEncoder encoder, PasswordPolicy policy) {
        this.props = props;
        this.users = users;
        this.encoder = encoder;
        this.policy = policy;
    }

    @Override
    public void run(ApplicationArguments args) {
        String email = props.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email)) {
            return;
        }
        List<String> problems = policy.check(props.password(), email, "Admin", "ACM");
        if (!problems.isEmpty()) {
            throw new IllegalStateException("ADMIN_PASSWORD rejected: " + problems);
        }
        User admin = new User(email, encoder.encode(props.password()), "Admin", "ACM", "00000000");
        admin.addRole(Role.ADMIN);
        users.save(admin);
    }
}