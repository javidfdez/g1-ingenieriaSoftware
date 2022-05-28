package ingsoftware.fuengiuma.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Lugares {

    @Id
    private short latitud;
    @Id
    private short longitud;
    private String nombre;

    @ManyToMany(mappedBy = "origen")
    private List<Viaje> viajesOrigen;

    @ManyToMany(mappedBy = "destino")
    private List<Viaje> viajesDestino;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lugares lugares = (Lugares) o;

        if (latitud != lugares.latitud) return false;
        if (longitud != lugares.longitud) return false;
        return nombre != null ? nombre.equals(lugares.nombre) : lugares.nombre == null;
    }

    @Override
    public int hashCode() {
        int result = latitud;
        result = 31 * result + (int) longitud;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }
}
