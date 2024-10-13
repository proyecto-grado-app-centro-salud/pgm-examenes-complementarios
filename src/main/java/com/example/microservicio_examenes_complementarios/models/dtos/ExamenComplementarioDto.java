package com.example.microservicio_examenes_complementarios.models.dtos;

import java.util.Date;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamenComplementarioDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String resumenResultados;
    private Date fechaResultado;
    private Integer idHistoriaClinica;
    private Integer idMedico;
    private Date fechaSolicitud;
    private String enlaceArchivoResultado;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public ExamenComplementarioDto convertirExamenComplementarioEntityAExamenComplementarioDto(ExamenComplementarioEntity examen) {
        ExamenComplementarioDto dto = new ExamenComplementarioDto();
        dto.setId(examen.getIdExamenComplementario());
        dto.setNombre(examen.getNombre());
        dto.setDescripcion(examen.getDescripcion());
        dto.setResumenResultados(examen.getResumenResultados());
        dto.setFechaResultado(examen.getFechaResultado());
        dto.setIdHistoriaClinica(examen.getHistoriaClinica().getIdHistoriaClinica());
        dto.setIdMedico(examen.getMedico().getIdUsuario());
        dto.setFechaSolicitud(examen.getFechaSolicitud());
        dto.setEnlaceArchivoResultado(examen.getEnlaceArchivoResultado());
        dto.setCreatedAt(examen.getCreatedAt());
        dto.setUpdatedAt(examen.getUpdatedAt());
        dto.setDeletedAt(examen.getDeletedAt());
        return dto;
    }
}