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
public class ItemPedidoId implements Serializable {
    @Column(name = "pedido_id")
    private Long pedidoId;
    @Column(name = "produto_id")
    private Long produtoId;
}
