package com.acm.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PasswordPolicy {

    private static final int MIN_LENGTH = 12;
    private static final int MAX_BYTES = 72;   // BCrypt ignores everything after 72 bytes

    private final Set<String> common;

    public PasswordPolicy() {
        try (var in = new ClassPathResource("common-passwords.txt").getInputStream();
             var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            common = reader.lines()
                    .map(String::trim)
                    .filter(l -> !l.isEmpty() && !l.startsWith("#"))
                    .map(l -> l.toLowerCase(Locale.ROOT))
                    .collect(Collectors.toUnmodifiableSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Returns the list of problems (empty = password accepted). */
    public List<String> check(String password, String email, String prenom, String nom) {
        List<String> problems = new ArrayList<>();

        if (password.codePointCount(0, password.length()) < MIN_LENGTH) {
            problems.add("Le mot de passe doit contenir au moins " + MIN_LENGTH + " caractères.");
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES) {
            problems.add("Le mot de passe est trop long (72 octets maximum).");
        }

        String lower = password.toLowerCase(Locale.ROOT);
        String alnum = lower.replaceAll("[^\\p{L}\\p{N}]", "");
        String base = lower.replaceAll("[^\\p{L}]+$", "");   // "Password123!!" -> "password"

        if (common.contains(lower) || common.contains(alnum) || (base.length() >= 4 && common.contains(base))) {
            problems.add("Ce mot de passe est trop courant.");
        }
        if (password.chars().distinct().count() < 6 || isSequential(alnum)) {
            problems.add("Évitez les répétitions et les suites de caractères (aaaa…, 1234…, abcd…).");
        }

        String local = email != null && email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        for (String part : Arrays.asList(local, prenom, nom)) {
            if (part == null) continue;
            String p = part.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}]", "");
            if (p.length() >= 4 && alnum.contains(p)) {
                problems.add("Le mot de passe ne doit pas contenir votre nom ou votre e-mail.");
                break;
            }
        }
        return problems;
    }

    private static boolean isSequential(String s) {
        if (s.length() < 4) return false;
        int step = s.charAt(1) - s.charAt(0);
        if (Math.abs(step) != 1) return false;
        for (int i = 2; i < s.length(); i++) {
            if (s.charAt(i) - s.charAt(i - 1) != step) return false;
        }
        return true;
    }

    public static boolean tooLongForBcrypt(String password) {
        return password.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES;
    }
}