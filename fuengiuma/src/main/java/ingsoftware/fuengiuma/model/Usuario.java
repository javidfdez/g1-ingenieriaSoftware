package ingsoftware.fuengiuma.model;

import javax.persistence.*;

import java.util.Objects;

@Entity
public class Usuario {

    private String nombre;

    @Id
    @GeneratedValue
    private Integer id;

    private String telefono;
    private String contrasena;
    private Boolean visto = true;

//    @OneToMany(mappedBy = "conductor")
//    private Viaje conduce;

    public Usuario(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
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

//    public Viaje getConduce() {
//        return conduce;
//    }
//
//    public void setConduce(Viaje conduce) {
//        this.conduce = conduce;
//    }

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
