package edu.kalum.core.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "aspirante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aspirante implements Serializable {
    @Id
    @NotEmpty(message = "El campo no de Expediente no debe de ser vacio")
    @Column(name = "no_expediente")
    private String noExpediente;
    @NotEmpty(message = "El campo apellido aspirante, no puede ser vacio")
    @NotNull(message = "El apellido del aspirante no es valido")
    @Column(name = "apellidos")
    private String apellidos;
    @NotEmpty(message = "El campo nombre aspirante, no puede ser vacio")
    @NotNull(message = "El Nombre del aspirante no es valido")
    @Column(name = "nombres")
    private String nombres;
    @NotEmpty(message = "El campo direccion aspirante, no puede ser vacio")
    @NotNull(message = "La direccion del aspirante no es valida")
    @Column(name = "direccion")
    private String direccion;
    @NotEmpty(message = "El campo telefono aspirante, no pueden ser vacio")
    @NotNull(message = "El telefono del aspirante no es valido")
    @Column(name = "telefono")
    private String telefono;
    @NotEmpty(message = "El campo Email aspirante, no pueden ser vacio")
    @NotNull(message = "El Email del aspirante no es valido")
    @Email
    @Column(name = "email")
    private String email;
    @Column(name = "estatus")
    private String estatus = "NO ASIGNADO";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrera_id", referencedColumnName = "carrera_id")
    private CarreraTecnica carreraTecnica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jornada_id", referencedColumnName = "jornada_id")
    private Jornada jornada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examen_id", referencedColumnName = "examen_id")
    private ExamenAdmision examenAdmision;








}
