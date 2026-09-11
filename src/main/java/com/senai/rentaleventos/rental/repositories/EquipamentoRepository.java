package com.senai.rentaleventos.rental.repositories;

import com.senai.rentaleventos.rental.models.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
            String marca,
            String modelo,
            String categoria
    );

}

