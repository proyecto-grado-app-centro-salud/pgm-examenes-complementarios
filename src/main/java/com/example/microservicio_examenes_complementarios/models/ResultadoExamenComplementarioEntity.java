package com.example.microservicio_examenes_complementarios.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "resultados_examen_complementario")
public class ResultadoExamenComplementarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado_examen_complementario")
    private int idResultadoExamenComplementario;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "resumen_resultado")
    private String resumenResultado;

    @Column(name = "fecha_resultado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResultado;

    @Column(name = "enlace_archivo_resultado")
    private String enlaceArchivoResultado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_examen_complementario", nullable = false)
    private ExamenComplementarioEntity examenComplementario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "key_archivo")
    private String keyArchivo;

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
