package com.clara.hopelife.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String nome;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(name = "exige_receita", nullable = false)
    private boolean exigeReceita = false;
}
