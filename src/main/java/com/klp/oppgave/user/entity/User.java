package com.klp.oppgave.user.entity;

import jakarta.persistence.*;

import java.util.Set;
import java.util.regex.Pattern;

@Entity
@Table(name = "users")
public class User {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String type;

    public User() {}

    public User(String email, String type) {
        setEmail(email);
        setType(type);
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        Set<String> validTypes = Set.of("USER", "ADMIN");
        if (!validTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid user type: " + type);
        }
        this.type = type;
    }
}
