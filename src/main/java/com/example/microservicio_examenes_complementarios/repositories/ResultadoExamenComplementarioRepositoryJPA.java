package com.example.microservicio_examenes_complementarios.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.ResultadoExamenComplementarioEntity;

public interface ResultadoExamenComplementarioRepositoryJPA 
        extends JpaRepository<ResultadoExamenComplementarioEntity, Integer>, JpaSpecificationExecutor<ResultadoExamenComplementarioEntity> {

    Optional<ResultadoExamenComplementarioEntity> findByIdResultadoExamenComplementarioAndDeletedAtIsNull(int id);

    List<ResultadoExamenComplementarioEntity> findAllByDeletedAtIsNull();

    List<ResultadoExamenComplementarioEntity> findAllByExamenComplementarioAndDeletedAtIsNull(
            ExamenComplementarioEntity examenComplementarioEntity);
}
