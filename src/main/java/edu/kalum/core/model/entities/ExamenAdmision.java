package edu.kalum.core.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "examen_admision")
public class ExamenAdmision implements Serializable {
    @Id
    @Column(name = "examen_id")
    String examenId;
    @Column(name = "fecha_examen")
    Timestamp fechaExamen;
}
