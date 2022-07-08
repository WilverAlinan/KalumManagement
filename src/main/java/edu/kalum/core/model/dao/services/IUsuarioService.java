package edu.kalum.core.model.dao.services;

import edu.kalum.core.model.entities.Usuario;

public interface IUsuarioService {
    public Usuario findBydUsername(String username);
    public Usuario findByEmail(String email);

}
