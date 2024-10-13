package com.example.microservicio_examenes_complementarios.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.microservicio_examenes_complementarios.models.dtos.ExamenComplementarioDto;
import com.example.microservicio_examenes_complementarios.services.ContainerMetadataService;
import com.example.microservicio_examenes_complementarios.services.ExamenesComplementariosService;



@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/examenes-complementarios")
public class ExamenesComplementariosController {
    @Autowired
    private ContainerMetadataService containerMetadataService;
    @Autowired
    private ExamenesComplementariosService examenesComplementariosService;

    @PostMapping
    public ResponseEntity<ExamenComplementarioDto> registrarExamenComplementario(@RequestBody ExamenComplementarioDto examenDto) {
        try {
            ExamenComplementarioDto nuevoExamen = examenesComplementariosService.registrarExamenComplementario(examenDto);
            return new ResponseEntity<>(nuevoExamen, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ExamenComplementarioDto>> obtenerTodosExamenesComplementarios() {
        try {
            List<ExamenComplementarioDto> examenes = examenesComplementariosService.obtenerTodosExamenesComplementarios();
            return new ResponseEntity<>(examenes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamenComplementarioDto> obtenerExamenComplementarioPorId(@PathVariable Integer id) {
        try {
            ExamenComplementarioDto examen = examenesComplementariosService.obtenerExamenComplementarioPorId(id);
            return new ResponseEntity<>(examen, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamenComplementarioDto> actualizarExamenComplementario(@PathVariable Integer id, @RequestBody ExamenComplementarioDto examenDto) {
        try {
            ExamenComplementarioDto examenActualizado = examenesComplementariosService.actualizarExamenComplementario(id, examenDto);
            return new ResponseEntity<>(examenActualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<ExamenComplementarioDto>> obtenerExamenesDePaciente(@PathVariable int idPaciente) {
        try {
            List<ExamenComplementarioDto> examenes = examenesComplementariosService.obtenerExamenesDePaciente(idPaciente);
            return new ResponseEntity<>(examenes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/info-container")
    public @ResponseBody String obtenerInformacionContenedor() {
        return "microservicio historias clinicas: " + containerMetadataService.retrieveContainerMetadataInfo();
    }
}