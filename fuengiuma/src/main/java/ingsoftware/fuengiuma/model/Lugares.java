package ingsoftware.fuengiuma.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Lugares {

    @Id
    @GeneratedValue
    private Integer id;

    private short latitud;
    private short longitud;

    private String nombre;

    @ManyToMany
    private List<Viaje> viajesOrigen;

    @ManyToMany
    private List<Viaje> viajesDestino;

    public Lugares(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
