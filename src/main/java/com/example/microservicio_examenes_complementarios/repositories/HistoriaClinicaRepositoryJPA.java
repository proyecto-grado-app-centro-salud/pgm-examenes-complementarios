package com.example.microservicio_examenes_complementarios.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservicio_examenes_complementarios.models.HistoriaClinicaEntity;
import com.example.microservicio_examenes_complementarios.models.UsuarioEntity;


public interface HistoriaClinicaRepositoryJPA extends JpaRepository<HistoriaClinicaEntity, Integer> {

    List<HistoriaClinicaEntity> findByPaciente(UsuarioEntity paciente);
}