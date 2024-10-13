package com.example.microservicio_examenes_complementarios.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;

public interface ExamenesComplementariosRepositoryJPA extends JpaRepository<ExamenComplementarioEntity, Integer> {
    @Query("SELECT ec FROM ExamenComplementarioEntity ec "
    + "JOIN ec.historiaClinica hc "
    + "JOIN hc.paciente p "
    + "WHERE p.idUsuario = :idPaciente")
    List<ExamenComplementarioEntity> obtenerExamenesComplementariosPaciente(@Param("idPaciente") int idPaciente);

}