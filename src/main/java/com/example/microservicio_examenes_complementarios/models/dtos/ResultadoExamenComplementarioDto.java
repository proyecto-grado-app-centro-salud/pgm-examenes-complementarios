package com.example.microservicio_examenes_complementarios.models.dtos;

import java.util.Date;

import com.example.microservicio_examenes_complementarios.models.ResultadoExamenComplementarioEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoExamenComplementarioDto {

    private Integer idResultadoExamenComplementario;
    private String titulo;
    private String resumenResultado;
    private Date fechaResultado;
    private String enlaceArchivoResultado;
    private int idExamenComplementario;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private String keyArchivo;

    public ResultadoExamenComplementarioDto convertirEntityADto(ResultadoExamenComplementarioEntity resultadoExamenComplementarioEntity) {
        ResultadoExamenComplementarioDto dto = new ResultadoExamenComplementarioDto();
        dto.setTitulo(resultadoExamenComplementarioEntity.getTitulo());
        dto.setIdResultadoExamenComplementario(resultadoExamenComplementarioEntity.getIdResultadoExamenComplementario());
        dto.setResumenResultado(resultadoExamenComplementarioEntity.getResumenResultado());
        dto.setFechaResultado(resultadoExamenComplementarioEntity.getFechaResultado());
        dto.setEnlaceArchivoResultado(resultadoExamenComplementarioEntity.getEnlaceArchivoResultado());
        dto.setIdExamenComplementario(resultadoExamenComplementarioEntity.getExamenComplementario().getIdExamenComplementario());
        dto.setCreatedAt(resultadoExamenComplementarioEntity.getCreatedAt());
        dto.setUpdatedAt(resultadoExamenComplementarioEntity.getUpdatedAt());
        dto.setDeletedAt(resultadoExamenComplementarioEntity.getDeletedAt());
        dto.setKeyArchivo(resultadoExamenComplementarioEntity.getKeyArchivo());
        return dto;
    }
}