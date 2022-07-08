package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.Alumno;
import edu.kalum.core.model.entities.CarreraTecnica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlumnoService {
    public List<Alumno> findAll();
    public Page<Alumno> findAll(Pageable pageable);
    public Alumno findById(String alumnoId);
    public Alumno save(Alumno alumno);
    public void delete(Alumno alumno);

}
