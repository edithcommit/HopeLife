package com.clara.hopelife.repository;

import com.clara.hopelife.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByUsuario_Id(Long usuarioId);

    Optional<Pedido> findByMercadoPagoOrderId(String mercadoPagoOrderId);

    Optional<Pedido> findByIdAndUsuario_Id(Long id, Long usuarioId);
}
