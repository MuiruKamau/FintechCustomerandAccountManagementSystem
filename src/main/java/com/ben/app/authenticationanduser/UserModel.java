package com.ben.app.authenticationanduser;

import jakarta.persistence.*;
import lombok.*;




@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


}













