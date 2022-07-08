package edu.kalum.core.model.dao.services;

import edu.kalum.core.model.entities.CarreraTecnica;
import edu.kalum.core.model.entities.Inscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InscripcionService {

    public List<Inscripcion> findAll();
    public Page<Inscripcion> findAll(Pageable pageable);
    public  Inscripcion findById(String inscripcionId);
    public Inscripcion save(Inscripcion inscripcion);
    public void delete(Inscripcion inscripcion);
}
