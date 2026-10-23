package com.clara.hopelife.repository;

import com.clara.hopelife.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, FavoritoId> {
    List<ProdutoFavorito> findByUsuarioId(Long usuarioId);
}
