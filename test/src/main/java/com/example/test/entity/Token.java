package com.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tokens")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @Column(name = "refreshToken", length = 250)
    private String refreshToken;

    @Column(name = "expiresIn", length = 64)
    private String expiresIn;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
