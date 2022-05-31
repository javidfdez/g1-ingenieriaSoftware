package ingsoftware.fuengiuma.service;

import ingsoftware.fuengiuma.model.Usuario;
import ingsoftware.fuengiuma.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    public List<Usuario> getAll(){
        return personaRepository.findAll();
    }
}
