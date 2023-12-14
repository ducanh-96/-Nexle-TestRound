package com.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name = "firstName", length = 32)
    private String firstName;

    @Column(name = "lastName", length = 32)
    private String lastName;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public void setPassword(String encode) {
        this.hash = encode;
    }

    private String displayName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}
