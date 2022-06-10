package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.Alumno;
import java.util.List;

public interface IAlumnoService {
    public List<Alumno> findAll();
    public Alumno findById(String alumnoId);

}
