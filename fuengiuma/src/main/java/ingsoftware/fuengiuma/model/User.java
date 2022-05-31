package ingsoftware.fuengiuma.model;

import javax.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class User {

    private String nombre;

    @Id
    @GeneratedValue
    private int id;

    private String telefono;
    private String contrasena;
    private Boolean visto = True;
    
	@ManyToMany (mappedBy= "pasajeros")
	private List<Viaje> viajesPasajeros;
	
	@OneToMany (mappedBy= "conductor")
	private List<Viaje> viajesConductor;

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

    public User(String nombre, String telefono, String contrasena) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.contraseña = contraseña;
    }

    
    public String getNombre() {
        return nombre;
    }

    public Integer getTelefono() {
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

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;


    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setVisto(Boolean visto) {
        this.visto = visto;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        User user = (User) object;
        return telefono.equals(user.telefono);
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
