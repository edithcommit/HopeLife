
package com.clara.hopelife.repository;

import com.clara.hopelife.model.Farmaceutico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmaceuticoRepository
        extends JpaRepository<Farmaceutico, Long> {

    boolean existsByMatricula(String matricula);

    boolean existsByUsuario_Id(Long usuarioId);

    boolean existsByUsuario_IdAndMatricula(Long usuarioId, String matricula);
}
