package com.example.microservicio_examenes_complementarios.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservicio_examenes_complementarios.models.EspecialidadesEntity;

import java.util.List;
import java.util.Optional;

public interface EspecialidadesRepositoryJPA extends JpaRepository<EspecialidadesEntity, Integer> {
    List<EspecialidadesEntity> findAllByDeletedAtIsNull();
    Optional<EspecialidadesEntity> findByIdEspecialidadAndDeletedAtIsNull(int idEspecialidad);
}