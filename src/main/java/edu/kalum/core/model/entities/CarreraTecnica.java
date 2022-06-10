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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="carrera_tecnica")
public class CarreraTecnica implements Serializable {
    @Id
    @Column(name="carrera_id")
    public String carreraId;
    @Column(name = "carrera_tecnica")
    public String carreraTecnica;

}
