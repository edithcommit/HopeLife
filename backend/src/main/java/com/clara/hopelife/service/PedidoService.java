package com.clara.hopelife.service;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.exception.ApiException;
import com.clara.hopelife.model.*;
import com.clara.hopelife.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.clara.hopelife.config.MercadoPagoProperties;
import com.clara.hopelife.integration.mercadopago.MercadoPagoClient;
import com.clara.hopelife.integration.mercadopago.dto.MercadoPagoDtos.CriarOrderRequest;
import com.clara.hopelife.integration.mercadopago.dto.MercadoPagoDtos.CriarOrderResponse;
import com.clara.hopelife.integration.mercadopago.dto.MercadoPagoDtos.Payer;
import com.clara.hopelife.integration.mercadopago.dto.MercadoPagoDtos.Item;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidos;
    private final ProdutoRepository produtos;
    private final UsuarioService usuarios;
    private final MercadoPagoClient mercadoPagoClient;
    private final MercadoPagoProperties mercadoPagoProperties;

    @Transactional
    public PedidoView checkout(String email, Checkout req) {
        Usuario u = usuarios.obter(email);

        if (u.getPerfil() != Perfil.CLIENTE) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "Checkout exclusivo para clientes");
        }

        Pedido p = new Pedido();
        p.setUsuario(u);

        boolean receita = false;
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemCheckout dto : req.itens()) {
            Produto prod = produtos.findById(dto.produtoId())
                    .orElseThrow(() -> new ApiException(
                            HttpStatus.NOT_FOUND,
                            "Produto não encontrado: " + dto.produtoId()));

            if (!prod.isAtivo()) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "Produto indisponível: " + prod.getNome());
            }

            receita |= prod.isExigeReceita();

            ItemPedido i = new ItemPedido();
            i.setId(new ItemPedidoId(null, prod.getId()));
            i.setPedido(p);
            i.setProduto(prod);
            i.setQuantidade(dto.quantidade());
            i.setPrecoUnitario(prod.getPreco());

            BigDecimal subtotal = prod.getPreco()
                    .multiply(BigDecimal.valueOf(dto.quantidade()));

            valorTotal = valorTotal.add(subtotal);
            p.getItens().add(i);
        }

        if (receita && (req.urlReceita() == null
                || req.urlReceita().isBlank())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "URL da receita obrigatória para este pedido");
        }

        p.setUrlReceita(receita ? req.urlReceita() : null);
        p.setValorTotal(valorTotal);

        p.setStatus(receita
                ? StatusPedido.PENDENTE_RECEITA
                : StatusPedido.PENDENTE_PAGAMENTO);

        // Persiste o pedido para obter seu ID.
        pedidos.save(p);

        // Pedidos sem exigência de receita seguem para pagamento.
        if (!receita) {
            String emailComprador = mercadoPagoProperties.getTestPayerEmail();

            if (emailComprador == null || emailComprador.isBlank()) {
                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "E-mail do comprador de teste não configurado");
            }

            String chaveIdempotencia = UUID.randomUUID().toString();
            p.setChaveIdempotencia(chaveIdempotencia);

            List<Item> itensMercadoPago = p.getItens().stream()
                    .map(i -> new Item(
                            i.getProduto().getNome(),
                            i.getQuantidade(),
                            i.getPrecoUnitario().toPlainString()))
                    .toList();

            CriarOrderRequest orderRequest = new CriarOrderRequest(
                    "online",
                    valorTotal.toPlainString(),
                    String.valueOf(p.getId()),
                    "manual",
                    new Payer(emailComprador),
                    itensMercadoPago);

            CriarOrderResponse resposta = mercadoPagoClient.criarOrder(
                    orderRequest,
                    chaveIdempotencia);

            if (resposta == null
                    || resposta.id() == null
                    || resposta.checkoutUrl() == null) {
                throw new ApiException(
                        HttpStatus.BAD_GATEWAY,
                        "Mercado Pago não retornou os dados da ordem");
            }

            p.setMercadoPagoOrderId(resposta.id());
            p.setCheckoutUrl(resposta.checkoutUrl());

            pedidos.save(p);
        }

        return view(p);
    }

        @Transactional(readOnly = true)
        public PedidoView buscarPorId(Long id, String email) {

        Usuario usuario = usuarios.obter(email);

        Pedido pedido = pedidos.findByIdAndUsuario_Id(
                id,
                usuario.getId()
        ).orElseThrow(() -> new ApiException(
                HttpStatus.NOT_FOUND,
                "Pedido não encontrado"
        ));

        return view(pedido);
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
        List<ItemView> its = p.getItens().stream()
                .map(i -> new ItemView(
                        i.getProduto().getId(),
                        i.getProduto().getNome(),
                        i.getQuantidade(),
                        i.getPrecoUnitario()))
                .toList();

        return new PedidoView(
                p.getId(),
                p.getStatus().name(),
                p.getUrlReceita(),
                p.getDataCriacao(),
                p.getValorTotal(),
                p.getCheckoutUrl(),
                its);
    }


        public void processarNotificacaoMercadoPago(String orderId) {

                // Consulta a ordem diretamente na API do Mercado Pago.
                CriarOrderResponse ordem = mercadoPagoClient.buscarOrder(orderId);

                if (ordem == null || ordem.id() == null) {
                        return;
                }

                // Confere se a ordem retornada é a mesma da notificação.
                if (!ordem.id().equals(orderId)) {
                        return;
                }

                // Só considera pagamentos efetivamente aprovados.
                if (!"processed".equals(ordem.status())
                        || !"accredited".equals(ordem.statusDetail())) {
                        return;
                }

                if (ordem.externalReference() == null) {
                        return;
                }

                Long pedidoId;

                try {
                        pedidoId = Long.valueOf(ordem.externalReference());
                } catch (NumberFormatException e) {
                        return;
                }

                Pedido pedido = pedidos.findByMercadoPagoOrderId(orderId)
                        .orElse(null);

                if (pedido == null) {
                        return;
                }

                // Confere se a referência externa corresponde ao pedido encontrado.
                if (!pedido.getId().equals(pedidoId)) {
                        return;
                }

                // Notificação repetida não deve alterar novamente o pedido.
                if (pedido.getStatus() == StatusPedido.PAGO) {
                        return;
                }

                if (pedido.getStatus() != StatusPedido.PENDENTE_PAGAMENTO) {
                        return;
                }

                // Confere o valor efetivamente pago.
                if (ordem.totalPaidAmount() == null) {
                        return;
                }

                BigDecimal valorPago;

                try {
                        valorPago = new BigDecimal(ordem.totalPaidAmount());
                } catch (NumberFormatException e) {
                        return;
                }

                if (valorPago.compareTo(pedido.getValorTotal()) != 0) {
                        return;
                }

                pedido.setStatus(StatusPedido.PAGO);
                pedido.setDataPagamento(LocalDateTime.now());

                pedidos.save(pedido);
        }

}
