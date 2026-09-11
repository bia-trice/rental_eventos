package com.senai.rentaleventos.rental.services;

import com.senai.rentaleventos.rental.models.Equipamento;
import com.senai.rentaleventos.rental.models.Movimentacao;
import com.senai.rentaleventos.rental.models.Usuario;
import com.senai.rentaleventos.rental.repositories.EquipamentoRepository;
import com.senai.rentaleventos.rental.repositories.MovimentacaoRepository;
import com.senai.rentaleventos.rental.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;

    private final EquipamentoRepository equipamentoRepository;

    private final UsuarioRepository usuarioRepository;

    public MovimentacaoService(
            MovimentacaoRepository movimentacaoRepository,
            EquipamentoRepository equipamentoRepository,
            UsuarioRepository usuarioRepository) {

        this.movimentacaoRepository = movimentacaoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Equipamento> listarEquipamentosOrdenados() {

        List<Equipamento> equipamentos =
                equipamentoRepository.findAll();

        /*
         * Algoritmo de ordenação utilizando Comparator.
         * A ordenação será feita pelo nome do modelo.
         */
        equipamentos.sort(
                Comparator.comparing(
                        Equipamento::getModelo,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return equipamentos;
    }

    public Equipamento buscarEquipamento(Long equipamentoId) {

        return equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipamento não encontrado."
                        )
                );
    }

    public Movimentacao registrar(
            Long equipamentoId,
            Movimentacao movimentacao) {

        validarMovimentacao(movimentacao);

        Equipamento equipamento =
                buscarEquipamento(equipamentoId);

        Usuario usuario =
                buscarUsuario(movimentacao);

        String tipo =
                movimentacao.getTipo().toUpperCase();

        if (!tipo.equals("ENTRADA") &&
                !tipo.equals("SAIDA")) {

            throw new RuntimeException(
                    "O tipo deve ser ENTRADA ou SAIDA."
            );
        }

        if (tipo.equals("ENTRADA")) {

            equipamento.setQuantidadeDisponivel(
                    equipamento.getQuantidadeDisponivel()
                            + movimentacao.getQuantidade()
            );
        }

        if (tipo.equals("SAIDA")) {

            if (movimentacao.getQuantidade()
                    > equipamento.getQuantidadeDisponivel()) {

                throw new RuntimeException(
                        "Não é possível realizar a saída. "
                                + "A quantidade solicitada é maior "
                                + "que o estoque disponível."
                );
            }

            equipamento.setQuantidadeDisponivel(
                    equipamento.getQuantidadeDisponivel()
                            - movimentacao.getQuantidade()
            );
        }

        equipamentoRepository.save(equipamento);

        movimentacao.setEquipamento(equipamento);

        movimentacao.setUsuario(usuario);

        movimentacao.setTipo(tipo);

        Movimentacao movimentacaoSalva =
                movimentacaoRepository.save(movimentacao);

        /*
         * Verificação automática do estoque mínimo
         * após uma movimentação de saída.
         */
        if (tipo.equals("SAIDA")) {

            verificarEstoqueMinimo(equipamento);
        }

        return movimentacaoSalva;
    }

    private Usuario buscarUsuario(
            Movimentacao movimentacao) {

        if (movimentacao.getUsuario() == null ||
                movimentacao.getUsuario().getId() == null) {

            throw new RuntimeException(
                    "O usuário responsável deve ser informado."
            );
        }

        return usuarioRepository.findById(
                movimentacao.getUsuario().getId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Usuário responsável não encontrado."
                )
        );
    }

    private void verificarEstoqueMinimo(
            Equipamento equipamento) {

        if (equipamento.getQuantidadeDisponivel()
                < equipamento.getQuantidadeMinima()) {

            System.out.println(
                    "ALERTA DE ESTOQUE: "
                            + equipamento.getMarca()
                            + " "
                            + equipamento.getModelo()
                            + " está abaixo do estoque mínimo. "
                            + "Disponível: "
                            + equipamento.getQuantidadeDisponivel()
                            + " | Mínimo: "
                            + equipamento.getQuantidadeMinima()
            );
        }
    }

    public List<Movimentacao> historico(
            Long equipamentoId) {

        Equipamento equipamento =
                buscarEquipamento(equipamentoId);

        return movimentacaoRepository
                .findByEquipamento(equipamento);
    }

    private void validarMovimentacao(
            Movimentacao movimentacao) {

        if (movimentacao == null) {

            throw new RuntimeException(
                    "Os dados da movimentação devem ser informados."
            );
        }

        if (movimentacao.getTipo() == null ||
                movimentacao.getTipo().isBlank()) {

            throw new RuntimeException(
                    "O tipo da movimentação deve ser informado."
            );
        }

        if (movimentacao.getQuantidade() == null ||
                movimentacao.getQuantidade() <= 0) {

            throw new RuntimeException(
                    "A quantidade deve ser maior que zero."
            );
        }

        if (movimentacao.getDataMovimentacao() == null) {

            throw new RuntimeException(
                    "A data da movimentação deve ser informada."
            );
        }
    }
}
