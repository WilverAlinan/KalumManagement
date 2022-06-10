package edu.kalum.core.model.dao.services;

import edu.kalum.core.model.dao.ICarreraTecnicaDao;
import edu.kalum.core.model.entities.CarreraTecnica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CarreraTecnicaServiceImpl implements ICarreraTecnicaService{
    @Autowired
    private ICarreraTecnicaDao carreraTecnicaDao;

    @Override
    public List<CarreraTecnica> findAll() {
        return carreraTecnicaDao.findAll();
    }

    @Override
    public CarreraTecnica findById(String carreraId) {
        return carreraTecnicaDao.findById(carreraId).orElse(null);
    }

}
