package com.clara.hopelife.service;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.exception.ApiException;
import com.clara.hopelife.model.*;
import com.clara.hopelife.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.clara.hopelife.security.JwtService;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final FarmaceuticoRepository farmaceuticos;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final MatriculaFuncionarioRepository matriculas;

    @Transactional
    public UsuarioView cadastrar(Cadastro d) {
        Usuario u = new Usuario();
        u.setNome(d.nome());
        u.setEmail(d.email());
        u.setSenhaHash(encoder.encode(d.senha()));
        u.setEndereco(d.endereco());
        u.setPerfil(Perfil.CLIENTE);
        return view(usuarios.save(u));
    }

    @Transactional
    public UsuarioView cadastrarFarmaceutico(CadastroFarmaceutico d) {
        String numeroMatricula = d.matricula().trim();

        MatriculaFuncionario autorizacao = matriculas
                .buscarParaUtilizacao(numeroMatricula)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.FORBIDDEN,
                        "Matrícula não autorizada para cadastro"));

        if (autorizacao.isUtilizada()) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Matrícula já utilizada");
        }

        if (farmaceuticos.existsByMatricula(numeroMatricula)) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Matrícula já cadastrada");
        }

        Usuario u = new Usuario();
        u.setNome(d.nome());
        u.setEmail(d.email());
        u.setSenhaHash(encoder.encode(d.senha()));
        u.setPerfil(Perfil.FARMACEUTICO);

        u = usuarios.save(u);

        Farmaceutico f = new Farmaceutico();
        f.setUsuario(u);
        f.setMatricula(numeroMatricula);

        farmaceuticos.save(f);

        autorizacao.setUtilizada(true);
        matriculas.save(autorizacao);

        return view(u);
    }

    @Transactional
    public UsuarioView atualizar(String email, PerfilUpdate d) {
        Usuario u = obter(email);
        u.setNome(d.nome());
        u.setEndereco(d.endereco());
        if (d.senha() != null && !d.senha().isBlank())
            u.setSenhaHash(encoder.encode(d.senha()));
        return view(u);
    }

    public Usuario obter(String email) {
        return usuarios.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private UsuarioView view(Usuario u) {
        return new UsuarioView(u.getId(), u.getNome(), u.getEmail(), u.getEndereco(), u.getPerfil().name());
    }

    public LoginResponse login(String email, String senha) {
        Usuario u = usuarios.findByEmail(email)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"));

        if (!encoder.matches(senha, u.getSenhaHash())) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(
                u.getEmail(),
                u.getPerfil().name());

        return new LoginResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getEndereco(),
                u.getPerfil().name(),
                token);
    }

    public LoginResponse loginFarmaceutico(
            String email,
            String senha,
            String matricula) {

        Usuario u = usuarios.findByEmail(email.trim())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "Credenciais inválidas"));

        if (!encoder.matches(senha, u.getSenhaHash())) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciais inválidas");
        }

        boolean matriculaValida = u.getPerfil() == Perfil.FARMACEUTICO
                && farmaceuticos.existsByUsuario_IdAndMatricula(
                        u.getId(),
                        matricula.trim());

        if (!matriculaValida) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciais inválidas");
        }

        String token = jwtService.gerarToken(
                u.getEmail(),
                u.getPerfil().name());

        return new LoginResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getEndereco(),
                u.getPerfil().name(),
                token);
    }

}