package ingsoftware.fuengiuma.model;

import javax.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Usuario {

    private String nombre;

    @Id
    @GeneratedValue
    private Integer id;

    private Integer telefono;
    private String contrasena;
    private Boolean visto = true;

    @ManyToMany (mappedBy= "pasajeros")
    private List<Viaje> viajesPasajeros;

    @OneToMany (mappedBy= "conductor")
    private List<Viaje> viajesConductor;

    public Usuario(){

    }

    public List<Viaje> getViajesPasajeros() {
        return viajesPasajeros;
    }

    public List<Viaje> getViajesConductor() {
        return viajesConductor;
    }

    public void setViajesConductor(List<Viaje> viajesConductor) {
        this.viajesConductor = viajesConductor;
    }

    public void setViajesPasajeros(List<Viaje> viajesPasajeros) {
        this.viajesPasajeros = viajesPasajeros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getVisto() {
        return visto;
    }

    public void setVisto(Boolean visto) {
        this.visto = visto;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario user = (Usuario) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                ", telefono=" + telefono +
                '}';
    }
}
