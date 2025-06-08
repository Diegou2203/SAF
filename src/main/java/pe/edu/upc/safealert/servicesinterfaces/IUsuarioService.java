package pe.edu.upc.safealert.servicesinterfaces;

import pe.edu.upc.safealert.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

    public List<Usuario> list();
    public void insert(Usuario u);
    public Usuario listarId(int idUsuario);
    public void delete(int idUsuario);
    public void update(Usuario fn);
    boolean existsByUsername(String username);
    public boolean isUsernameDuplicatedOnUpdate(String username, int idUsuario);


    public List<String[]> findUsuariosEnZonasDeAltoRiesgo();
}
