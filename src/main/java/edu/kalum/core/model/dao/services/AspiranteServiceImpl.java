package edu.kalum.core.model.dao.services;

import edu.kalum.core.model.dao.IAspiranteDao;
import edu.kalum.core.model.entities.Aspirante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AspiranteServiceImpl implements IAspiranteService{
    @Autowired
    private IAspiranteDao aspiranteDao;

    @Override
    public List<Aspirante> findAll() {
        return aspiranteDao.findAll();
    }

    @Override
    public Page<Aspirante> findAll(Pageable pageable) {
        return aspiranteDao.findAll(pageable);
    }

    @Override
    public Aspirante findById(String noExpediente) {
        return aspiranteDao.findById(noExpediente).orElse(null);
    }

    @Override
    public Aspirante save(Aspirante aspirante) {
        return aspiranteDao.save(aspirante);
    }

    @Override
    public void delete(Aspirante aspirante) {
        aspiranteDao.delete(aspirante);
    }
}
