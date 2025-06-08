package pe.edu.upc.safealert.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.safealert.entities.Usuario;
import pe.edu.upc.safealert.repositories.IUsuarioRepository;
import pe.edu.upc.safealert.servicesinterfaces.IUsuarioService;

import java.util.List;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private IUsuarioRepository uR;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> list() {return uR.findAll();}

    @Override
    public void insert(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        uR.save(u);
    }

    @Override
    public Usuario listarId(int idUsuario) {return uR.findById(idUsuario).orElse(new Usuario());}

    @Override
    public void delete(int idUsuario) {uR.deleteById(idUsuario);}

    @Override
    public void update(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        uR.save(u);
    }

    @Override
    public List<String[]> findUsuariosEnZonasDeAltoRiesgo() {
        return uR.findUsuariosEnZonasDeAltoRiesgo();
    }

    @Override
    public boolean existsByUsername(String username) {
        return uR.existsByUsername(username);
    }

    @Override
    public boolean isUsernameDuplicatedOnUpdate(String username, int idUsuario) {
        Usuario existente = uR.findByUsername(username);
        return existente != null && existente.getIdUsuario() != idUsuario;
    }

}