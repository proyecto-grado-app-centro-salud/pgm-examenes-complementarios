package com.example.microservicio_examenes_complementarios.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.ResultadoExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.dtos.ResultadoExamenComplementarioDto;
import com.example.microservicio_examenes_complementarios.repositories.ExamenesComplementariosRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.ResultadoExamenComplementarioRepositoryJPA;

@Service
public class ResultadosExamenComplementarioService {
    @Autowired
    private ConverterVariablesService converterVariablesService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ResultadoExamenComplementarioRepositoryJPA resultadoExamenComplementarioRepository;

    @Autowired
    private ExamenesComplementariosRepositoryJPA examenesComplementariosRepositoryJPA;

    public List<ResultadoExamenComplementarioDto> obtenerResultados() {
        List<ResultadoExamenComplementarioEntity> resultadosEntities = resultadoExamenComplementarioRepository.findAllByDeletedAtIsNull();
        return resultadosEntities.stream().map(resultadoEntities -> new ResultadoExamenComplementarioDto().convertirEntityADto(resultadoEntities)).toList();
    }
    public List<ResultadoExamenComplementarioDto> obtenerResultadosDeExamen(int idExamenComplementario) {
        ExamenComplementarioEntity examenComplementarioEntity = examenesComplementariosRepositoryJPA.findByIdExamenComplementarioAndDeletedAtIsNull(idExamenComplementario).orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        List<ResultadoExamenComplementarioEntity> resultadosEntities = resultadoExamenComplementarioRepository.findAllByExamenComplementarioAndDeletedAtIsNull(examenComplementarioEntity);
        return resultadosEntities.stream().map(resultadoEntities -> new ResultadoExamenComplementarioDto().convertirEntityADto(resultadoEntities)).toList();
    }

    public ResultadoExamenComplementarioDto obtenerResultadoPorId(int id) {
        ResultadoExamenComplementarioEntity resultadoEntity = resultadoExamenComplementarioRepository
                .findByIdResultadoExamenComplementarioAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));
        return new ResultadoExamenComplementarioDto().convertirEntityADto(resultadoEntity);
    }

    public ResultadoExamenComplementarioDto crearResultado(ResultadoExamenComplementarioDto resultadoDto,MultipartFile file) throws IOException {
        ExamenComplementarioEntity examenComplementarioEntity = examenesComplementariosRepositoryJPA.findByIdExamenComplementarioAndDeletedAtIsNull(resultadoDto.getIdExamenComplementario()).orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));

        ResultadoExamenComplementarioEntity resultadoEntity = new ResultadoExamenComplementarioEntity();
        resultadoEntity.setTitulo(resultadoDto.getTitulo());
        resultadoEntity.setResumenResultado(resultadoDto.getResumenResultado());
        resultadoEntity.setFechaResultado(resultadoDto.getFechaResultado());
        resultadoEntity.setExamenComplementario(examenComplementarioEntity);
        String key = s3Service.uploadFileResultadoExamenComplementario(file);        
        resultadoEntity.setKeyArchivo(key);
        resultadoEntity.setEnlaceArchivoResultado(converterVariablesService.convertirKeyImagenAEnlaceImagen(key));

        ResultadoExamenComplementarioEntity savedEntity = resultadoExamenComplementarioRepository.save(resultadoEntity);
        return new ResultadoExamenComplementarioDto().convertirEntityADto(savedEntity);
    }

    public ResultadoExamenComplementarioDto actualizarResultado(int id, ResultadoExamenComplementarioDto resultadoDto,MultipartFile file) throws IOException {
        ResultadoExamenComplementarioEntity resultadoEntity = resultadoExamenComplementarioRepository
                .findByIdResultadoExamenComplementarioAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));
        ExamenComplementarioEntity examenComplementarioEntity = examenesComplementariosRepositoryJPA.findByIdExamenComplementarioAndDeletedAtIsNull(resultadoDto.getIdExamenComplementario()).orElseThrow(() -> new RuntimeException("Examen complementario no encontrado"));
        resultadoEntity.setTitulo(resultadoDto.getTitulo());
        resultadoEntity.setResumenResultado(resultadoDto.getResumenResultado());
        resultadoEntity.setFechaResultado(resultadoDto.getFechaResultado());
        resultadoEntity.setExamenComplementario(examenComplementarioEntity);
        if(!converterVariablesService.arreglarFileName("resultados-examenes-complementarios/"+file.getOriginalFilename()).equals(resultadoEntity.getKeyArchivo())){
            s3Service.deleteFile(resultadoEntity.getKeyArchivo());
            String key = s3Service.uploadFileResultadoExamenComplementario(file);
            resultadoEntity.setKeyArchivo(key);
            resultadoEntity.setEnlaceArchivoResultado(converterVariablesService.convertirKeyImagenAEnlaceImagen(key));
        }
        ResultadoExamenComplementarioEntity updatedEntity = resultadoExamenComplementarioRepository.save(resultadoEntity);
        return new ResultadoExamenComplementarioDto().convertirEntityADto(updatedEntity);
    }

    public void eliminarResultado(int id) {
        ResultadoExamenComplementarioEntity resultadoEntity = resultadoExamenComplementarioRepository
                .findByIdResultadoExamenComplementarioAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));
        resultadoEntity.markAsDeleted();
        resultadoExamenComplementarioRepository.save(resultadoEntity);
    }

}