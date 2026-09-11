package com.senai.rentaleventos.rental.services;

import com.senai.rentaleventos.rental.models.Equipamento;
import com.senai.rentaleventos.rental.repositories.EquipamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoService(
            EquipamentoRepository equipamentoRepository) {

        this.equipamentoRepository = equipamentoRepository;
    }

    // Cadastrar equipamento
    public Equipamento cadastrar(Equipamento equipamento) {

        validarEquipamento(equipamento);

        return equipamentoRepository.save(equipamento);
    }

    // Listar todos os equipamentos
    public List<Equipamento> listarTodos() {

        return equipamentoRepository.findAll();
    }

    // Buscar equipamento pelo ID
    public Equipamento buscarPorId(Long id) {

        return equipamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipamento não encontrado."
                        )
                );
    }

    // Buscar equipamento por termo
    public List<Equipamento> buscarPorTermo(String termo) {

        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }

        return equipamentoRepository
                .findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
                        termo,
                        termo,
                        termo
                );
    }

    // Atualizar equipamento
    public Equipamento atualizar(
            Long id,
            Equipamento equipamento) {

        Equipamento equipamentoExistente =
                buscarPorId(id);

        validarEquipamento(equipamento);

        equipamentoExistente.setMarca(
                equipamento.getMarca()
        );

        equipamentoExistente.setModelo(
                equipamento.getModelo()
        );

        equipamentoExistente.setCategoria(
                equipamento.getCategoria()
        );

        equipamentoExistente.setPotencia(
                equipamento.getPotencia()
        );

        equipamentoExistente.setMaterial(
                equipamento.getMaterial()
        );

        equipamentoExistente.setPeso(
                equipamento.getPeso()
        );

        equipamentoExistente.setDimensoes(
                equipamento.getDimensoes()
        );

        equipamentoExistente.setCor(
                equipamento.getCor()
        );

        equipamentoExistente.setQuantidadeDisponivel(
                equipamento.getQuantidadeDisponivel()
        );

        equipamentoExistente.setQuantidadeMinima(
                equipamento.getQuantidadeMinima()
        );

        return equipamentoRepository.save(
                equipamentoExistente
        );
    }

    // Excluir equipamento
    public void excluir(Long id) {

        Equipamento equipamento =
                buscarPorId(id);

        equipamentoRepository.delete(equipamento);
    }

    // Salvar alterações no equipamento
    public Equipamento salvar(Equipamento equipamento) {

        return equipamentoRepository.save(equipamento);
    }

    // Validação dos dados
    private void validarEquipamento(
            Equipamento equipamento) {

        if (equipamento == null) {
            throw new RuntimeException(
                    "Os dados do equipamento devem ser informados."
            );
        }

        if (equipamento.getMarca() == null ||
                equipamento.getMarca().isBlank()) {

            throw new RuntimeException(
                    "A marca deve ser informada."
            );
        }

        if (equipamento.getModelo() == null ||
                equipamento.getModelo().isBlank()) {

            throw new RuntimeException(
                    "O modelo deve ser informado."
            );
        }

        if (equipamento.getCategoria() == null ||
                equipamento.getCategoria().isBlank()) {

            throw new RuntimeException(
                    "A categoria deve ser informada."
            );
        }

        if (equipamento.getQuantidadeDisponivel() == null ||
                equipamento.getQuantidadeDisponivel() < 0) {

            throw new RuntimeException(
                    "A quantidade disponível deve ser maior ou igual a zero."
            );
        }

        if (equipamento.getQuantidadeMinima() == null ||
                equipamento.getQuantidadeMinima() < 0) {

            throw new RuntimeException(
                    "A quantidade mínima deve ser maior ou igual a zero."
            );
        }

        if (equipamento.getPeso() != null &&
                equipamento.getPeso() < 0) {

            throw new RuntimeException(
                    "O peso não pode ser negativo."
            );
        }
    }
}

