package edu.kalum.core.model.dao.services;

import edu.kalum.core.model.dao.IUsuarioDao;
import edu.kalum.core.model.entities.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public Usuario findBydUsername(String username) {
        return usuarioDao.findByUsername(username);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioDao.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        if (usuario == null){
            logger.error("Error en el Login, no existe el usuario".concat(username).concat(" en el sistema"));
            throw new UsernameNotFoundException("Error en el Login, no existe el usuario".concat(username).concat(" en el sistema"));
        }

        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());

        return new User(usuario.getUsername(), usuario.getPassword(),
                usuario.isEnabled(),
                true,
                true,
                true,
                authorities);
    }
}
