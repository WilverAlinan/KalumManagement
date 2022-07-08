package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.Jornada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IJornadaService {
    public List<Jornada> findAll();
    public Page<Jornada> findAll(Pageable pageable);
    public Jornada findById(String jornadaId);
    public Jornada save(Jornada jornada);
    public void delete(Jornada jornada);
}
