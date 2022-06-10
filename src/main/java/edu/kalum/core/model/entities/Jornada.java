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
@Table(name = "jornada")
public class Jornada implements Serializable {
    @Id
    @Column(name = "jornada_id")
    String jornadaId;
    @Column(name = "jornada")
    String jornada;
    @Column(name = "descripcion")
    String descripcion;
}
