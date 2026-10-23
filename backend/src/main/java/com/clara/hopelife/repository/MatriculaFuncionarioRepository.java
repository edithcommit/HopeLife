
package com.clara.hopelife.repository;

import com.clara.hopelife.model.MatriculaFuncionario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MatriculaFuncionarioRepository
        extends JpaRepository<MatriculaFuncionario, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT m FROM MatriculaFuncionario m
        WHERE m.matricula = :matricula
    """)
    Optional<MatriculaFuncionario> buscarParaUtilizacao(
            @Param("matricula") String matricula
    );
}
