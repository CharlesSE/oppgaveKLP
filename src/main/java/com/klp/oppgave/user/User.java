package com.klp.oppgave.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be a valid email address")
    @Column(nullable = false)
    private String email;
    @NotNull(message = "Type cannot be null")
    @Pattern(regexp = "USER|ADMIN", message = "Type must be either 'USER' or 'ADMIN'")
    @Column(nullable = false)
    private String type;

    public User() {}

    public User(String email, String type) {
        this.email = email;
        this.type = type;
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
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
