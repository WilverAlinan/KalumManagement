package edu.kalum.core.model.dao.services;
import edu.kalum.core.model.entities.Cargo;
import java.util.List;

public interface ICargoService {
    public List<Cargo> findAll();
    public Cargo findById(String cargoId);
}
