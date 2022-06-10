package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.CarreraTecnica;
import java.util.List;

public interface ICarreraTecnicaService {
    public List<CarreraTecnica> findAll();
    public  CarreraTecnica findById(String carreraId);
}
