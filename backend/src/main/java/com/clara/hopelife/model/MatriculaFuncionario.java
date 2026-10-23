
package com.clara.hopelife.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matriculas_funcionarios")
@Getter
@Setter
@NoArgsConstructor
public class MatriculaFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 7)
    private String matricula;

    @Column(nullable = false)
    private boolean utilizada = false;
}
