package com.clara.hopelife.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "farmaceuticos")

@Getter

@Setter
@NoArgsConstructor

public class Farmaceutico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)

    @JoinColumn(name = "usuario_id", nullable = false, unique = true)

    private Usuario usuario;
    @Column(nullable = false, length = 7, unique = true)
    private String matricula;
}
