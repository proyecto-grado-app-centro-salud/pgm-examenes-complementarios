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
    private Date fechaSolicitud;
    private String enlaceArchivoResultado;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Integer idHistoriaClinica;
    private String diagnosticoPresuntivo;
    private Integer idEspecialidad;
    private String nombreEspecialidad;
    private String idMedico;
    private String nombreMedico;
    private String idPaciente;
    private String pacientePropietario;
    private String ciPropietario;

    public static ExamenComplementarioDto convertirExamenComplementarioEntityAExamenComplementarioDto(ExamenComplementarioEntity examen) {
        ExamenComplementarioDto dto = new ExamenComplementarioDto();
        dto.setId(examen.getIdExamenComplementario());
        dto.setNombre(examen.getNombre());
        dto.setDescripcion(examen.getDescripcion());
        dto.setResumenResultados(examen.getResumenResultados());
        dto.setFechaResultado(examen.getFechaResultado());
        dto.setFechaSolicitud(examen.getFechaSolicitud());
        dto.setEnlaceArchivoResultado(examen.getEnlaceArchivoResultado());
        dto.setCreatedAt(examen.getCreatedAt());
        dto.setUpdatedAt(examen.getUpdatedAt());
        dto.setDeletedAt(examen.getDeletedAt());
        dto.setIdHistoriaClinica(examen.getHistoriaClinica().getIdHistoriaClinica());
        dto.setDiagnosticoPresuntivo(examen.getHistoriaClinica().getDiagnosticoPresuntivo());
        dto.setIdEspecialidad(examen.getHistoriaClinica().getEspecialidad().getIdEspecialidad());
        dto.setNombreEspecialidad(examen.getHistoriaClinica().getEspecialidad().getNombre());
        dto.setIdMedico(examen.getHistoriaClinica().getMedico().getIdUsuario());
        dto.setNombreMedico(examen.getMedico().getNombres() + " " + examen.getMedico().getApellidoPaterno()+ " " + examen.getMedico().getApellidoMaterno());
        dto.setIdPaciente(examen.getHistoriaClinica().getPaciente().getIdUsuario());
        dto.setPacientePropietario(examen.getHistoriaClinica().getPaciente().getNombres() + " " + examen.getHistoriaClinica().getPaciente().getApellidoPaterno()+ " " + examen.getHistoriaClinica().getPaciente().getApellidoMaterno());
        dto.setCiPropietario(examen.getHistoriaClinica().getPaciente().getCi());        
        return dto;
    }
}