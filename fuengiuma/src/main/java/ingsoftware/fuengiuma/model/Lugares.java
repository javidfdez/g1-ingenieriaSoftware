package ingsoftware.fuengiuma.model;

import java.util.List;
<<<<<<< HEAD
import java.util.Objects;
=======
>>>>>>> refs/remotes/origin/main

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Lugares {

    @Id
    @GeneratedValue
    private int id;
<<<<<<< HEAD

    private short latitud;
    private short longitud;

    private String nombre;

=======

    private short latitud;
    //@Id
    private short longitud;
    @Override
	public String toString() {
		return "Lugares [latitud=" + latitud + ", longitud=" + longitud + "]";
	}

	private String nombre;

>>>>>>> refs/remotes/origin/main
    @ManyToMany
    private List<Viaje> viajesOrigen;

    @ManyToMany
    private List<Viaje> viajesDestino;

<<<<<<< HEAD
    public Lugares(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
=======

    public Lugares(short latitud, short longitud, String nombre) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
>>>>>>> refs/remotes/origin/main
    }


    public short getLatitud() {
        return latitud;
    }

    public void setLatitud(short latitud) {
        this.latitud = latitud;
    }

    public short getLongitud() {
        return longitud;
    }

    public void setLongitud(short longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Viaje> getViajesOrigen() {
        return viajesOrigen;
    }

    public void setViajesOrigen(List<Viaje> viajesOrigen) {
        this.viajesOrigen = viajesOrigen;
    }

    public List<Viaje> getViajesDestino() {
        return viajesDestino;
    }

    public void setViajesDestino(List<Viaje> viajesDestino) {
        this.viajesDestino = viajesDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lugares lugares = (Lugares) o;
        return id == lugares.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Lugares{" +
                "id=" + id +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", nombre='" + nombre + '\'' +
                ", viajesOrigen=" + viajesOrigen +
                ", viajesDestino=" + viajesDestino +
                '}';
    }
}
