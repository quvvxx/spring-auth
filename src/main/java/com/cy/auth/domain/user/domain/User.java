package com.cy.auth.domain.user.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String username;

    @Column(nullable = false, length = 16)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

}
