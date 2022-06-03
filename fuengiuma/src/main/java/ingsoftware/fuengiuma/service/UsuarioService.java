package ingsoftware.fuengiuma.service;

import ingsoftware.fuengiuma.model.Usuario;
import ingsoftware.fuengiuma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Usuario> getAll(){
        return usuarioRepository.findAll();
    }

    public void save(Usuario u) {
        usuarioRepository.saveAndFlush(u);
    }

    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario getById(Integer id) {
        return usuarioRepository.getOne(id);
    }


}
