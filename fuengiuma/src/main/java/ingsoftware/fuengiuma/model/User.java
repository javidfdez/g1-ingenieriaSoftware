package ingsoftware.fuengiuma.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class User {

    private String nombre;

    @Id
    private Integer telefono;
    private String contraseña;
    private Boolean visto;

    public User(String nombre, Integer telefono, String contraseña) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.visto = true;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public Boolean getVisto() {
        return visto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setVisto(Boolean visto) {
        this.visto = visto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(telefono, user.telefono) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(telefono);
    }

    @Override
    public String toString() {
        return "User{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}
