package edu.kalum.core.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "examen_admision")
public class ExamenAdmision implements Serializable {
    @Id
    @Column(name = "examen_id")
    private String examenId;
    @Column(name = "fecha_examen")
    private Date fechaExamen;

    @OneToMany(mappedBy = "examenAdmision",fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private List<Aspirante> aspirantes;

}
