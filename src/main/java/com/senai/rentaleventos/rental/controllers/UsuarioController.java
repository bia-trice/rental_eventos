package com.senai.rentaleventos.rental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.senai.rentaleventos.rental.models.Usuario;
import com.senai.rentaleventos.rental.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String telaLogin() {

        return "login";
    }

    @PostMapping("/login")
    public String realizarLogin(
            @RequestParam String usuario,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        try {

            Usuario usuarioLogado =
                    usuarioService.autenticar(
                            usuario,
                            senha
                    );

            session.setAttribute(
                    "usuarioLogado",
                    usuarioLogado
            );

            return "redirect:/principal";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}

