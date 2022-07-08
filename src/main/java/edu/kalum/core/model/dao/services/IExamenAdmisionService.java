package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.ExamenAdmision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IExamenAdmisionService {
    public List<ExamenAdmision> findAll();
    public Page<ExamenAdmision> findAll(Pageable pageable);
    public ExamenAdmision findById(String examenId);
    public ExamenAdmision save(ExamenAdmision examenAdmision);
    public void delete(ExamenAdmision examenAdmision);
}
