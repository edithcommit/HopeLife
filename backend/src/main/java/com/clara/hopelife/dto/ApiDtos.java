package com.clara.hopelife.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class ApiDtos {
        private ApiDtos() {
        }

        public record Cadastro(@NotBlank @Size(max = 100) String nome, @NotBlank @Email @Size(max = 150) String email,
                        @NotBlank @Size(min = 8, max = 100) String senha, @Size(max = 255) String endereco) {
        }

        public record CadastroFarmaceutico(@NotBlank @Size(max = 100) String nome,
                        @NotBlank @Email @Size(max = 150) String email,
                        @NotBlank @Size(min = 8, max = 100) String senha,
                        @NotBlank @Pattern(regexp = "^[0-9]-[0-9]{5}$") String matricula) {
        }

        public record PerfilUpdate(@NotBlank @Size(max = 100) String nome, @Size(max = 255) String endereco,
                        @Size(min = 8, max = 100) String senha) {
        }

        public record UsuarioView(Long id, String nome, String email, String endereco, String perfil) {
        }

        public record ProdutoView(Long id, String nome, BigDecimal preco, boolean exigeReceita) {
        }

        public record ItemCheckout(@NotNull Long produtoId, @NotNull @Positive Integer quantidade) {
        }

        public record Checkout(@NotEmpty List<@NotNull @Valid ItemCheckout> itens, String urlReceita) {
        }

        public record ItemView(Long produtoId, String nome, Integer quantidade, BigDecimal precoUnitario) {
        }

        public record PedidoView(Long id, String status, String urlReceita, LocalDateTime dataCriacao,
                        List<ItemView> itens) {
        }

        public record LoginRequest(String email, String senha) {
        }

        public record LoginResponse(Long id, String nome, String email, String endereco, String perfil, String token) {
        }

        public record LoginFarmaceuticoRequest(@NotBlank @Email @Size(max = 150) String email, @NotBlank String senha, @NotBlank
        @Pattern(regexp = "^[0-9]-[0-9]{5}$")
        String matricula) {
        }
}
