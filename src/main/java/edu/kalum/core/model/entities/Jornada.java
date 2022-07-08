package edu.kalum.core.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jornada")
public class Jornada implements Serializable {
    @Id
    @Column(name = "jornada_id")
    private String jornadaId;
    @NotEmpty
    @Column(name = "jornada")
    private String jornada;
    @NotEmpty
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "jornada",fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private List<Aspirante> aspirantes;

}
