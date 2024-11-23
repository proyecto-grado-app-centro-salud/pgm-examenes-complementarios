package com.example.microservicio_examenes_complementarios.controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.microservicio_examenes_complementarios.models.dtos.ResultadoExamenComplementarioDto;
import com.example.microservicio_examenes_complementarios.services.ResultadosExamenComplementarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/v1.0/resultados-examen-complementario")
public class ResultadosExamenComplementarioController {

    @Autowired
    private ResultadosExamenComplementarioService resultadoExamenComplementarioService;

    
    private final ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping
    public ResponseEntity<List<ResultadoExamenComplementarioDto>> obtenerResultados() {
        try {
            List<ResultadoExamenComplementarioDto> resultados = resultadoExamenComplementarioService.obtenerResultados();
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoExamenComplementarioDto> obtenerResultadoPorId(@PathVariable int id) {
        try {
            ResultadoExamenComplementarioDto resultado = resultadoExamenComplementarioService.obtenerResultadoPorId(id);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/examenes-complementarios/{idExamenComplementario}")
    public ResponseEntity<List<ResultadoExamenComplementarioDto>> obtenerResultadosDeExamen(@PathVariable int idExamenComplementario) {
        try {
            List<ResultadoExamenComplementarioDto> resultado = resultadoExamenComplementarioService.obtenerResultadosDeExamen(idExamenComplementario);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<ResultadoExamenComplementarioDto> crearResultado(
           @RequestParam("data") String data,
            @RequestParam("file") MultipartFile file) {

        try {
           ResultadoExamenComplementarioDto resultadoDto = objectMapper.readValue(data, ResultadoExamenComplementarioDto.class);
            ResultadoExamenComplementarioDto resultadoCreado = resultadoExamenComplementarioService.crearResultado(resultadoDto,file);
            return new ResponseEntity<>(resultadoCreado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoExamenComplementarioDto> actualizarResultado(
            @PathVariable int id,
            @RequestParam("data") String data,
            @RequestParam("file") MultipartFile file) {

        try {
            ResultadoExamenComplementarioDto resultadoDto = objectMapper.readValue(data, ResultadoExamenComplementarioDto.class);

            ResultadoExamenComplementarioDto resultadoActualizado = resultadoExamenComplementarioService.actualizarResultado(id, resultadoDto, file);

            return new ResponseEntity<>(resultadoActualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResultado(@PathVariable int id) {
        try {
            resultadoExamenComplementarioService.eliminarResultado(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}