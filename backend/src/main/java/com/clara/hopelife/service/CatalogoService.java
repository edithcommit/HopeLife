package com.clara.hopelife.service;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.exception.ApiException;
import com.clara.hopelife.model.*;
import com.clara.hopelife.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoService {
    private final ProdutoRepository produtos;
    private final ProdutoFavoritoRepository favoritos;
    private final UsuarioService usuarios;

    public List<ProdutoView> listar() {
        return produtos.findAll().stream()
                .map(p -> new ProdutoView(p.getId(), p.getNome(), p.getPreco(), p.isExigeReceita())).toList();
    }

    @Transactional
    public void favoritar(String email, Long id) {
        Usuario u = usuarios.obter(email);
        if (u.getPerfil() != Perfil.CLIENTE)
            throw new ApiException(HttpStatus.FORBIDDEN, "Ação exclusiva para clientes");
        Produto p = produtos.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        FavoritoId key = new FavoritoId(u.getId(), id);
        if (!favoritos.existsById(key))
            favoritos.save(new ProdutoFavorito(u, p));
    }

    @Transactional
    public void desfavoritar(String email, Long id) {
        Usuario u = usuarios.obter(email);
        if (u.getPerfil() != Perfil.CLIENTE)
            throw new ApiException(HttpStatus.FORBIDDEN, "Ação exclusiva para clientes");
        favoritos.deleteById(new FavoritoId(u.getId(), id));
    }

    public List<ProdutoView> favoritos(String email) {
        Usuario u = usuarios.obter(email);
        return favoritos.findByUsuarioId(u.getId()).stream().map(f -> f.getProduto())
                .map(p -> new ProdutoView(p.getId(), p.getNome(), p.getPreco(), p.isExigeReceita())).toList();
    }
}
