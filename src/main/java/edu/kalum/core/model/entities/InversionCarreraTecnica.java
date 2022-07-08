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
@Table(name = "inversion_carrera_tecnica")
public class InversionCarreraTecnica implements Serializable {
    @Id
    @Column(name = "inversion_id")
    public String inversionId;
    @Column(name = "carrera_id")
    public String carreraId;
    @Column(name = "monto_inscripcion")
    public String montoInscripcion;
    @Column(name = "numero_pagos")
    public int numeroPagos;
    @Column(name = "monto_pago")
    public String montoPagos;
}
