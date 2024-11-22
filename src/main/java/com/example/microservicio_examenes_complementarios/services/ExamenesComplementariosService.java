package com.example.microservicio_examenes_complementarios.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.HistoriaClinicaEntity;
import com.example.microservicio_examenes_complementarios.models.UsuarioEntity;
import com.example.microservicio_examenes_complementarios.models.dtos.ExamenComplementarioDto;
import com.example.microservicio_examenes_complementarios.repositories.ExamenesComplementariosRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.HistoriaClinicaRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.UsuariosRepositoryJPA;

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
    public ExamenComplementarioDto registrarExamenComplementario(ExamenComplementarioDto examenDto) {
        UsuarioEntity medicoEntity = usuariosRepositoryJPA.findById(examenDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findById(examenDto.getIdHistoriaClinica())
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

    public List<ExamenComplementarioDto> obtenerTodosExamenesComplementarios() {
        List<ExamenComplementarioEntity> examenes = examenComplementarioRepositoryJPA.findAll();
        return examenes.stream()
                .map(examen -> new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examen))
                .toList();
    }

    public ExamenComplementarioDto obtenerExamenComplementarioPorId(Integer id) {
        ExamenComplementarioEntity examenEntity = examenComplementarioRepositoryJPA.findById(id)
                .orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        return new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examenEntity);
    }

    public ExamenComplementarioDto actualizarExamenComplementario(Integer idExamen, ExamenComplementarioDto examenDto) {
        ExamenComplementarioEntity examenEntity = examenComplementarioRepositoryJPA.findById(idExamen)
                .orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        UsuarioEntity usuarioEntity = usuariosRepositoryJPA.findById(examenDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findById(examenDto.getIdHistoriaClinica())
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

    public List<ExamenComplementarioDto> obtenerExamenesDePaciente(int idPaciente) {
        List<ExamenComplementarioEntity> examenes = examenComplementarioRepositoryJPA.obtenerExamenesComplementariosPaciente(idPaciente);
        return examenes.stream()
                        .map(examen -> new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examen))
                        .toList();
    }

    public byte[] obtenerPDFExamenComplementario(ExamenComplementarioDto examenComplementarioDto) {
        try {
                return pdfService.generarPdfReporteExamenComplementario(examenComplementarioDto);
        } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al generar el PDF de la historia clinica.", e);
        }
    }
}