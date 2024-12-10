package com.example.microservicio_examenes_complementarios.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservicio_examenes_complementarios.models.EspecialidadesEntity;
import com.example.microservicio_examenes_complementarios.models.ExamenComplementarioEntity;
import com.example.microservicio_examenes_complementarios.models.HistoriaClinicaEntity;
import com.example.microservicio_examenes_complementarios.models.UsuarioEntity;
import com.example.microservicio_examenes_complementarios.models.dtos.ExamenComplementarioDto;
import com.example.microservicio_examenes_complementarios.repositories.EspecialidadesRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.ExamenesComplementariosRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.HistoriaClinicaRepositoryJPA;
import com.example.microservicio_examenes_complementarios.repositories.UsuariosRepositoryJPA;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PDFService {
    @Autowired
    UsuariosRepositoryJPA usuariosRepositoryJPA;
    @Autowired
    HistoriaClinicaRepositoryJPA historiaClinicaRepositoryJPA;
    @Autowired
    EspecialidadesRepositoryJPA especialidadesRepositoryJPA;
    @Autowired
    ExamenesComplementariosRepositoryJPA examenesComplementariosRepositoryJPA;

    public byte[] generarPdfReporteExamenComplementario(ExamenComplementarioDto examenComplementarioDto) throws JRException {
        Optional<ExamenComplementarioEntity> examenComplementarioEntityOptional=(examenComplementarioDto.getId()!=null)?examenesComplementariosRepositoryJPA.findById(examenComplementarioDto.getId()):Optional.empty();
        if(examenComplementarioEntityOptional.isPresent()){
            examenComplementarioDto=new ExamenComplementarioDto().convertirExamenComplementarioEntityAExamenComplementarioDto(examenComplementarioEntityOptional.get());
        }else{
            examenComplementarioDto.setCreatedAt(new Date());
            examenComplementarioDto.setUpdatedAt(new Date());
        }
        InputStream jrxmlInputStream = getClass().getClassLoader().getResourceAsStream("reports/examen_complementario.jrxml");
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findById(examenComplementarioDto.getIdHistoriaClinica()).orElseThrow(() -> new RuntimeException("Historia clinica no encontrada"));
        UsuarioEntity pacienteEntity = usuariosRepositoryJPA.findById(historiaClinicaEntity.getPaciente().getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UsuarioEntity medicoEntity = usuariosRepositoryJPA.findById(examenComplementarioDto.getIdMedico()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        EspecialidadesEntity especialidadesEntity = especialidadesRepositoryJPA.findById(historiaClinicaEntity.getEspecialidad().getIdEspecialidad()).orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        if (jrxmlInputStream == null) {
            throw new JRException("No se pudo encontrar el archivo .jrxml en el classpath.");
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nombre",examenComplementarioDto.getNombre());
        parameters.put("descripcion",examenComplementarioDto.getDescripcion());

        parameters.put("apellidoPaterno", pacienteEntity.getApellidoPaterno());
        parameters.put("apellidoMaterno", pacienteEntity.getApellidoMaterno());
        parameters.put("nombres", pacienteEntity.getNombres());
        parameters.put("nhc", historiaClinicaEntity.getIdHistoriaClinica()+"");
        parameters.put("edad", pacienteEntity.getEdad()+"");
        parameters.put("sexo", pacienteEntity.getSexo());
        parameters.put("estadoCivil", pacienteEntity.getEstadoCivil());
        parameters.put("unidad", especialidadesEntity.getNombre());



        parameters.put("fecha", formato.format(examenComplementarioDto.getUpdatedAt()));
        parameters.put("nombreCompletoPaciente", pacienteEntity.getNombres()+" "+pacienteEntity.getApellidoPaterno());
        parameters.put("nombreCompletoMedico", medicoEntity.getNombres()+" "+medicoEntity.getApellidoPaterno());
        parameters.put("firmaPaciente", "");
        parameters.put("firmaMedico", "");

        parameters.put("IMAGE_PATH", getClass().getClassLoader().getResource("images/logo.jpeg").getPath());
        List<ExamenComplementarioDto> list = new ArrayList<>();
        list.add(examenComplementarioDto);

        JRBeanCollectionDataSource emptyDataSource = new JRBeanCollectionDataSource(list);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, emptyDataSource);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        return outputStream.toByteArray();
    }
}
