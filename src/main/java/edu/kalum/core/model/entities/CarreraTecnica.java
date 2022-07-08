package edu.kalum.core.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="carrera_tecnica")
public class CarreraTecnica implements Serializable {
    //<carreraTecnica>
    @Id
    @Column(name="carrera_id")
    public String carreraId;

    @NotEmpty(message = "El Campo Carrera Tecninca, no puede ser vacio")
    @NotNull(message = "El Nombre de la Carrera Tecnica no es valido")
    @Column(name = "carrera_tecnica")
    public String carreraTecnica;

    @OneToMany(mappedBy = "carreraTecnica", fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    public List<Aspirante> aspirantes;


}
