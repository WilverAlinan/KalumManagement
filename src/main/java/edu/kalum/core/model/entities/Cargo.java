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
    private String cargoId;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "prefijo")
    private String prefijo;
    @Column(name = "monto")
    private float monto;
    @Column(name = "genera_mora")
    private byte generaMora;
    @Column(name = "porcentaje_mora")
    private int porcentajeMora;
}
