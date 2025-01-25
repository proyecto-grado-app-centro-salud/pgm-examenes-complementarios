package com.example.microservicio_examenes_complementarios.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.ResultadoExamenComplementarioEntity;

import jakarta.transaction.Transactional;

public interface ExamenesComplementariosRepositoryJPA extends JpaRepository<ExamenComplementarioEntity, Integer>,JpaSpecificationExecutor<ExamenComplementarioEntity> {
    @Query("SELECT ec FROM ExamenComplementarioEntity ec "
    + "JOIN ec.historiaClinica hc "
    + "JOIN hc.paciente p "
    + "WHERE p.idUsuario = :idPaciente")
    List<ExamenComplementarioEntity> obtenerExamenesComplementariosPaciente(@Param("idPaciente") int idPaciente);

    Optional<ExamenComplementarioEntity> findByIdExamenComplementarioAndDeletedAtIsNull(
            int idExamenComplementario);

    @Modifying
    @Query(value = "UPDATE examenes_complementarios SET deleted_at = ?2 WHERE id_historia_clinica = ?1", nativeQuery = true)
    void markAsDeletedAllExamenesComplementariosFromHistoriaClinica(int idHistoriaClinica, Date date);

}