package com.clara.hopelife.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FavoritoId implements Serializable {
    @Column(name = "usuario_id")
    private Long usuarioId;
    @Column(name = "produto_id")
    private Long produtoId;
}
