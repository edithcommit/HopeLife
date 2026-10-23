package com.clara.hopelife.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produtos_favoritos")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoFavorito {
    @EmbeddedId
    private FavoritoId id;
    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public ProdutoFavorito(Usuario usuario, Produto produto) {
        this.id = new FavoritoId(usuario.getId(), produto.getId());
        this.usuario = usuario;
        this.produto = produto;
    }
}
