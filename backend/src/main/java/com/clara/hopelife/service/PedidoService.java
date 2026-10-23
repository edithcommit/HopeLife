package com.clara.hopelife.service;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.exception.ApiException;
import com.clara.hopelife.model.*;
import com.clara.hopelife.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidos;
    private final ProdutoRepository produtos;
    private final UsuarioService usuarios;

    @Transactional
    public PedidoView checkout(String email, Checkout req) {
        Usuario u = usuarios.obter(email);
        if (u.getPerfil() != Perfil.CLIENTE)
            throw new ApiException(HttpStatus.FORBIDDEN, "Checkout exclusivo para clientes");
        Pedido p = new Pedido();
        p.setUsuario(u);
        boolean receita = false;
        for (ItemCheckout dto : req.itens()) {
            Produto prod = produtos.findById(dto.produtoId()).orElseThrow(
                    () -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + dto.produtoId()));
            receita |= prod.isExigeReceita();
            ItemPedido i = new ItemPedido();
            i.setId(new ItemPedidoId(null, prod.getId()));
            i.setPedido(p);
            i.setProduto(prod);
            i.setQuantidade(dto.quantidade());
            i.setPrecoUnitario(prod.getPreco());
            p.getItens().add(i);
        }
        if (receita && (req.urlReceita() == null || req.urlReceita().isBlank()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "URL da receita obrigatória para este pedido");
        p.setUrlReceita(receita ? req.urlReceita() : null);
        p.setStatus(receita ? StatusPedido.PENDENTE_RECEITA : StatusPedido.PENDENTE_PAGAMENTO);
        return view(pedidos.save(p));
    }

    public List<PedidoView> pendentesReceita() {
        return pedidos.findByStatus(StatusPedido.PENDENTE_RECEITA).stream().map(this::view).toList();
    }

    @Transactional
    public PedidoView decidir(Long id, boolean aprovar) {
        Pedido p = pedidos.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        if (p.getStatus() != StatusPedido.PENDENTE_RECEITA)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Pedido não está pendente de receita");
        p.setStatus(aprovar ? StatusPedido.APROVADO : StatusPedido.REJEITADO);
        return view(p);
    }

    private PedidoView view(Pedido p) {
        List<ItemView> its = p.getItens().stream().map(i -> new ItemView(i.getProduto().getId(),
                i.getProduto().getNome(), i.getQuantidade(), i.getPrecoUnitario())).toList();
        return new PedidoView(p.getId(), p.getStatus().name(), p.getUrlReceita(), p.getDataCriacao(), its);
    }
}
