package com.acm.mail;

import com.acm.config.AdminProperties;
import com.acm.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** F3: e-mail the admin after each registration, once the account is committed, without ever blocking or failing it. */
@Component
public class RegistrationMailListener {

    private static final Logger log = LoggerFactory.getLogger(RegistrationMailListener.class);

    private final ObjectProvider<JavaMailSender> mailSender;
    private final AdminProperties admin;
    private final String from;

    public RegistrationMailListener(ObjectProvider<JavaMailSender> mailSender, AdminProperties admin,
                                    @Value("${spring.mail.username:}") String from) {
        this.mailSender = mailSender;
        this.admin = admin;
        this.from = from;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRegistered(UserRegisteredEvent e) {
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            log.warn("SMTP non configuré : e-mail d'inscription non envoyé.");
            return;
        }
        try {
            SimpleMailMessage m = new SimpleMailMessage();
            if (!from.isBlank()) m.setFrom(from);
            m.setTo(admin.email());
            m.setSubject("ACM - Nouvelle inscription");   // fixed subject: no user input in headers
            m.setText("Un nouveau compte COLLABORATEUR vient d'être créé.\n\n"
                    + "Nom : " + e.prenom() + " " + e.nom() + "\n"
                    + "E-mail : " + e.email() + "\n");
            sender.send(m);
        } catch (Exception ex) {
            log.warn("Envoi de l'e-mail à l'administrateur impossible : {}", ex.getMessage());
        }
    }
}