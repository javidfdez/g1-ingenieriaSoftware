package ingsoftware.fuengiuma.repository;

import ingsoftware.fuengiuma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Usuario, Integer> {

}
