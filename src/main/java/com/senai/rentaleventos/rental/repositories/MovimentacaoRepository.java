package com.senai.rentaleventos.rental.repositories;

import com.senai.rentaleventos.rental.models.Equipamento;
import com.senai.rentaleventos.rental.models.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByEquipamento(Equipamento equipamento);

}

