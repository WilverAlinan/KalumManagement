package edu.kalum.core.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alumno")
public class Alumno implements Serializable {

    @Id
    @Column(name = "carne")
    public String carne;
    @OneToMany(mappedBy = "carne",fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private List<Inscripcion> inscripcion;

    @NotEmpty
    @Column(name = "apellidos")
    private String apellidos;
    @NotEmpty
    @Column(name = "nombres")
    private String nombres;
    @NotEmpty
    @Column(name = "direccion")
    private String direccion;
    @NotEmpty
    @Column(name = "telefono")
    private String telefono;
    @Email
    @Column(name = "email")
    private String email;
}
