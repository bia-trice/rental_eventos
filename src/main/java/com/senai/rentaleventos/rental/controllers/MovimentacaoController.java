package com.senai.rentaleventos.rental.controllers;

import com.senai.rentaleventos.rental.models.Movimentacao;
import com.senai.rentaleventos.rental.services.MovimentacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gestao")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(
            MovimentacaoService movimentacaoService) {

        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/equipamentos")
    public String telaGestao(Model model) {

        model.addAttribute(
                "equipamentos",
                movimentacaoService.listarEquipamentosOrdenados()
        );

        return "gestao/equipamentos";
    }

    @GetMapping("/movimentacao/{equipamentoId}")
    public String telaMovimentacao(
            @PathVariable Long equipamentoId,
            Model model) {

        model.addAttribute(
                "equipamento",
                movimentacaoService.buscarEquipamento(equipamentoId)
        );

        model.addAttribute(
                "movimentacao",
                new Movimentacao()
        );

        return "gestao/movimentacao";
    }

    @PostMapping("/movimentacao/{equipamentoId}")
    public String registrarMovimentacao(
            @PathVariable Long equipamentoId,
            @ModelAttribute Movimentacao movimentacao,
            Model model) {

        try {

            movimentacaoService.registrar(
                    equipamentoId,
                    movimentacao
            );

            return "redirect:/gestao/equipamentos";

        } catch (RuntimeException e) {

            model.addAttribute("erro", e.getMessage());

            model.addAttribute(
                    "equipamento",
                    movimentacaoService.buscarEquipamento(equipamentoId)
            );

            model.addAttribute(
                    "movimentacao",
                    movimentacao
            );

            return "gestao/movimentacao";
        }
    }


    @GetMapping("/historico/{equipamentoId}")
    public String historico(
            @PathVariable Long equipamentoId,
            Model model) {

        model.addAttribute(
                "equipamento",
                movimentacaoService.buscarEquipamento(equipamentoId)
        );

        model.addAttribute(
                "movimentacoes",
                movimentacaoService.historico(equipamentoId)
        );

        return "gestao/historico";
    }

    @GetMapping("/voltar")
    public String voltarPrincipal() {

        return "redirect:/principal";
    }
}

