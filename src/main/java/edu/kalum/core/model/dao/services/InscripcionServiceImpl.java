package edu.kalum.core.model.dao.services;

import edu.kalum.core.model.dao.IInscripcionDao;
import edu.kalum.core.model.entities.CarreraTecnica;
import edu.kalum.core.model.entities.Inscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InscripcionServiceImpl implements InscripcionService{

    @Autowired
    private IInscripcionDao inscripcionDao;

    @Override
    public List<Inscripcion> findAll() {
        return inscripcionDao.findAll();
    }

    @Override
    public Page<Inscripcion> findAll(Pageable pageable) {
        return inscripcionDao.findAll(pageable);
    }

    @Override
    public Inscripcion findById(String inscripcionId) {
        return inscripcionDao.findById(inscripcionId).orElse(null);
    }

    @Override
    public Inscripcion save(Inscripcion inscripcion) {
        return inscripcionDao.save(inscripcion);
    }

    @Override
    public void delete(Inscripcion inscripcion) {
        inscripcionDao.delete(inscripcion);
    }
}
