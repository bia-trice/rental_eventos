package com.senai.rentaleventos.rental.controllers;

import com.senai.rentaleventos.rental.models.Equipamento;
import com.senai.rentaleventos.rental.services.EquipamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    public String telaCadastro(
            @RequestParam(required = false) String busca,
            Model model) {

        if (busca != null && !busca.isBlank()) {
            model.addAttribute(
                    "equipamentos",
                    equipamentoService.buscarPorTermo(busca)
            );
        } else {
            model.addAttribute(
                    "equipamentos",
                    equipamentoService.listarTodos()
            );
        }

        model.addAttribute("equipamento", new Equipamento());

        return "equipamentos/cadastro";
    }

    @PostMapping
    public String cadastrar(
            @ModelAttribute Equipamento equipamento,
            Model model) {

        try {

            equipamentoService.cadastrar(equipamento);

            return "redirect:/equipamentos";

        } catch (RuntimeException e) {

            model.addAttribute("erro", e.getMessage());

            model.addAttribute(
                    "equipamentos",
                    equipamentoService.listarTodos()
            );

            return "equipamentos/cadastro";
        }
    }

    @GetMapping("/editar/{id}")
    public String telaEditar(
            @PathVariable Long id,
            Model model) {

        Equipamento equipamento =
                equipamentoService.buscarPorId(id);

        model.addAttribute("equipamento", equipamento);

        return "equipamentos/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Equipamento equipamento,
            Model model) {

        try {

            equipamentoService.atualizar(id, equipamento);

            return "redirect:/equipamentos";

        } catch (RuntimeException e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("equipamento", equipamento);

            return "equipamentos/editar";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        equipamentoService.excluir(id);

        return "redirect:/equipamentos";
    }

    @GetMapping("/voltar")
    public String voltarPrincipal() {

        return "redirect:/principal";
    }
}
