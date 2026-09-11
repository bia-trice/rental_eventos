package com.senai.rentaleventos.rental.services;

import com.senai.rentaleventos.rental.models.Usuario;
import com.senai.rentaleventos.rental.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario autenticar(String usuario, String senha) {

        if (usuario == null || usuario.isBlank()) {
            throw new RuntimeException("O usuário deve ser informado.");
        }

        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("A senha deve ser informada.");
        }

        Usuario usuarioEncontrado = usuarioRepository
                .findByUsuario(usuario)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado."
                        )
                );

        if (!usuarioEncontrado.getSenha().equals(senha)) {
            throw new RuntimeException(
                    "Senha incorreta."
            );
        }

        return usuarioEncontrado;
    }

    public Usuario cadastrar(Usuario usuario) {

        if (usuario.getNome() == null ||
                usuario.getNome().isBlank()) {

            throw new RuntimeException(
                    "O nome deve ser informado."
            );
        }

        if (usuario.getUsuario() == null ||
                usuario.getUsuario().isBlank()) {

            throw new RuntimeException(
                    "O usuário deve ser informado."
            );
        }

        if (usuario.getSenha() == null ||
                usuario.getSenha().isBlank()) {

            throw new RuntimeException(
                    "A senha deve ser informada."
            );
        }

        if (usuarioRepository
                .findByUsuario(usuario.getUsuario())
                .isPresent()) {

            throw new RuntimeException(
                    "Este usuário já está cadastrado."
            );
        }

        return usuarioRepository.save(usuario);
    }
}
