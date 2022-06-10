package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.ExamenAdmision;
import java.util.List;

public interface IExamenAdmisionService {
    public List<ExamenAdmision> findAll();
    public ExamenAdmision findById(String examenId);
}
