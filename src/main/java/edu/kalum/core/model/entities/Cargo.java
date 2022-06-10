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
@Table(name = "cargo")
public class Cargo implements Serializable {
    @Id
    @Column(name ="cargo_id")
    String cargoId;
    @Column(name = "descripcion")
    String descripcion;
    @Column(name = "prefijo")
    String prefijo;
    @Column(name = "monto")
    float monto;
    @Column(name = "genera_mora")
    byte generaMora;
    @Column(name = "porcentaje_mora")
    int porcentajeMora;
}
