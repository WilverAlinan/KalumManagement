package edu.kalum.core.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alumno")
public class Alumno implements Serializable {
    @Id
    @Column(name = "carne")
    String alumnoId;
    @Column(name = "apellidos")
    String apellidos;
    @Column(name = "nombres")
    String nombres;
    @Column(name = "direccion")
    String direccion;
    @Column(name = "telefono")
    String telefono;
    @Column(name = "email")
    String email;
}
