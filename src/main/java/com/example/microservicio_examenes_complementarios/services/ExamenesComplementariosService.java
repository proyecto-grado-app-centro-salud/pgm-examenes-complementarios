package com.example.microservicio_examenes_complementarios.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.HistoriaClinicaEntity;
import com.example.microservicio_examenes_complementarios.models.UsuarioEntity;
import com.example.microservicio_examenes_complementarios.models.dtos.ExamenComplementarioDto;
import com.example.microservicio_examenes_complementarios.repositories.ExamenesComplementariosRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.HistoriaClinicaRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.UsuariosRepositoryJPA;
import com.example.microservicio_examenes_complementarios.util.ExamenesComplementariosSpecification;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExamenesComplementariosService {

    @Autowired
    private ExamenesComplementariosRepositoryJPA examenComplementarioRepositoryJPA;

    @Autowired
    private HistoriaClinicaRepositoryJPA historiaClinicaRepositoryJPA;

    @Autowired
    private UsuariosRepositoryJPA usuariosRepositoryJPA;

    @Autowired
    PDFService pdfService;

    @Autowired
    private ConvertirTiposDatosService convertirTiposDatosService;
    public ExamenComplementarioDto registrarExamenComplementario(ExamenComplementarioDto examenDto) {
        UsuarioEntity medicoEntity = usuariosRepositoryJPA.findByIdUsuarioAndDeletedAtIsNull(examenDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findByIdHistoriaClinicaAndDeletedAtIsNull(examenDto.getIdHistoriaClinica())
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        ExamenComplementarioEntity examenEntity = new ExamenComplementarioEntity();
        examenEntity.setNombre(examenDto.getNombre());
        examenEntity.setDescripcion(examenDto.getDescripcion());
        examenEntity.setResumenResultados(examenDto.getResumenResultados());
        examenEntity.setFechaResultado(examenDto.getFechaResultado());
        examenEntity.setHistoriaClinica(historiaClinicaEntity);
        examenEntity.setMedico(medicoEntity);
        examenEntity.setFechaSolicitud(examenDto.getFechaSolicitud());
        examenEntity.setEnlaceArchivoResultado(examenDto.getEnlaceArchivoResultado());
        examenEntity = examenComplementarioRepositoryJPA.save(examenEntity);

        return new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examenEntity);
    }

    public Page<ExamenComplementarioDto> obtenerTodosExamenesComplementarios(String fechaInicio, String fechaFin, String ciPaciente, String nombrePaciente, String nombreMedico, String nombreEspecialidad, String diagnosticoPresuntivo, Integer page, Integer size) {
        List<ExamenComplementarioEntity> examenes = new ArrayList<>();
        Pageable pageable = Pageable.unpaged();
        if(page!=null && size!=null){
            pageable = PageRequest.of(page, size);
        } 
        Specification<ExamenComplementarioEntity> spec = Specification.where(ExamenesComplementariosSpecification.obtenerExamenesComplementariosPorParametros(convertirTiposDatosService.convertirStringADate(fechaInicio),convertirTiposDatosService.convertirStringADate(fechaFin),ciPaciente,nombrePaciente,nombreMedico,nombreEspecialidad,diagnosticoPresuntivo));
        Page<ExamenComplementarioEntity> examenesEntitiesPage=examenComplementarioRepositoryJPA.findAll(spec,pageable);
        return examenesEntitiesPage.map(ExamenComplementarioDto::convertirExamenComplementarioEntityAExamenComplementarioDto);
    }

    public ExamenComplementarioDto obtenerExamenComplementarioPorId(Integer id) {
        ExamenComplementarioEntity examenEntity = examenComplementarioRepositoryJPA.findByIdExamenComplementarioAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        return new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examenEntity);
    }

    public ExamenComplementarioDto actualizarExamenComplementario(Integer idExamen, ExamenComplementarioDto examenDto) {
        ExamenComplementarioEntity examenEntity = examenComplementarioRepositoryJPA.findByIdExamenComplementarioAndDeletedAtIsNull(idExamen)
                .orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        UsuarioEntity usuarioEntity = usuariosRepositoryJPA.findByIdUsuarioAndDeletedAtIsNull(examenDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findByIdHistoriaClinicaAndDeletedAtIsNull(examenDto.getIdHistoriaClinica())
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
        
        examenEntity.setNombre(examenDto.getNombre());
        examenEntity.setDescripcion(examenDto.getDescripcion());
        examenEntity.setResumenResultados(examenDto.getResumenResultados());
        examenEntity.setFechaResultado(examenDto.getFechaResultado());
        examenEntity.setFechaSolicitud(examenDto.getFechaSolicitud());
        examenEntity.setEnlaceArchivoResultado(examenDto.getEnlaceArchivoResultado());
        examenEntity.setHistoriaClinica(historiaClinicaEntity);
        examenEntity.setMedico(usuarioEntity);

        examenEntity = examenComplementarioRepositoryJPA.save(examenEntity);
        return new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examenEntity);
    }

    public Page<ExamenComplementarioDto> obtenerExamenesDePaciente(String idPaciente, String fechaInicio, String fechaFin, String nombreMedico, String nombreEspecialidad, String diagnosticoPresuntivo, Integer page, Integer size) {
        List<ExamenComplementarioEntity> examenes = new ArrayList<>();
        Pageable pageable = Pageable.unpaged();
        if(page!=null && size!=null){
            pageable = PageRequest.of(page, size);
        } 
        Specification<ExamenComplementarioEntity> spec = Specification.where(ExamenesComplementariosSpecification.obtenerExamenesComplementariosDePacientePorParametros(idPaciente,convertirTiposDatosService.convertirStringADate(fechaInicio),convertirTiposDatosService.convertirStringADate(fechaFin),nombreMedico,nombreEspecialidad,diagnosticoPresuntivo));
        Page<ExamenComplementarioEntity> examenesEntitiesPage=examenComplementarioRepositoryJPA.findAll(spec,pageable);
        return examenesEntitiesPage.map(ExamenComplementarioDto::convertirExamenComplementarioEntityAExamenComplementarioDto);
    }

    public byte[] obtenerPDFExamenComplementario(ExamenComplementarioDto examenComplementarioDto) {
        try {
                return pdfService.generarPdfReporteExamenComplementario(examenComplementarioDto);
        } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al generar el PDF de la historia clinica.", e);
        }
    }

    public void delete(int id) {
        ExamenComplementarioEntity examenEntity = examenComplementarioRepositoryJPA.findByIdExamenComplementarioAndDeletedAtIsNull(id)
        .orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        examenEntity.markAsDeleted();
        examenComplementarioRepositoryJPA.save(examenEntity);

    }
    
    @Transactional
    public void deleteExamenesComplementariosDeHistoriaClinica(int idHistoriaClinica) {
       examenComplementarioRepositoryJPA.markAsDeletedAllExamenesComplementariosFromHistoriaClinica(idHistoriaClinica,new Date());
    }
}