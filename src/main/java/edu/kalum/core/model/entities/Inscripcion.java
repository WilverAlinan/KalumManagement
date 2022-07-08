package edu.kalum.core.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inscripcion")
public class Inscripcion implements Serializable {

    @Id
    @Column(name = "inscripcion_id")
    private String inscripcionId;
    //muchos
    @Column(name = "carne")
    public String carne;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carne", referencedColumnName = "carne",insertable = false, updatable = false)
    private Alumno alumno;

    @Column(name = "carrera_id")
    private String carreraId;
    @Column(name = "jornada_id")
    private String jornadaId;
    @Column(name = "ciclo")
    private String ciclo;
    @Column(name = "fecha_inscripcion")
    private Date fechaInscripcion;


}
