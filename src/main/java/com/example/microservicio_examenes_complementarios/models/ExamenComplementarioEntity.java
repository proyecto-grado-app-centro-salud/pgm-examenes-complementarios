package com.example.microservicio_examenes_complementarios.models;


import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "examenes_complementarios")
public class ExamenComplementarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_examen_complementario")
    private int idExamenComplementario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "resumen_resultados")
    private String resumenResultados;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_resultado")
    private Date fechaResultado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia_clinica", nullable = false)
    private HistoriaClinicaEntity historiaClinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    private UsuarioEntity medico;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_solicitud", nullable = false)
    private Date fechaSolicitud;

    @Column(name = "enlace_archivo_resultado")
    private String enlaceArchivoResultado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public void markAsDeleted() {
        deletedAt = new Date();
    }
}
