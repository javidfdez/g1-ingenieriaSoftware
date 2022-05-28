package ingsoftware.fuengiuma.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
/*
 * 
 * Hecho por:
 * 
 * 
 * Iván Lago
 * Carlos Ríos
 * Javier Díaz
 * Jose Ismael Marinca 
 * 
 * 
 * 
 * 
 * 
 */
@Entity
public class Viaje {

	@Id
	@GeneratedValue
	private int código;
	
	private Lugares lugarOrigen;
	private Lugares lugarDestino;
	private Lugares puntosRecogida;
	
    //@DateTimeFormat(iso = ISO.DATE)
	//private Date HoraSalida;
//	private Date HoraLlegada;   
	private double precio;  
	private User conductor;   
	private String coche;    
	private short plazas; 
	private boolean visibilidad;
	
	/*@ManyToMany (mappedBy="viajesOrigen")
	private List<Lugares> origen;
	
	@ManyToMany (mappedBy="viajesDestino")
	private List<Lugares> destino;
	
	@ManyToMany (mappedBy= "viajePasajero")
	private List<User> pasajeros;
	
	@ManyToOne 
	private User conductor;
	*/
	
	
	
	public Viaje(int código, Lugares lugarOrigen, Lugares lugarDestino, Lugares puntosRecogida, double precio,
			User conductor, String coche, short plazas, boolean visibilidad) {
		super();
		this.código = código;
		this.lugarOrigen = lugarOrigen;
		this.lugarDestino = lugarDestino;
		this.puntosRecogida = puntosRecogida;
		this.precio = precio;
		this.conductor = conductor;
		this.coche = coche;
		this.plazas = plazas;
		this.visibilidad = visibilidad;
	}
	@Override
	public String toString() {
		return "Viaje [código=" + código + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + código;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viaje other = (Viaje) obj;
		if (código != other.código)
			return false;
		return true;
	}
	public int getCódigo() {
		return código;
	}
	public void setCódigo(int código) {
		this.código = código;
	}
	public Lugares getLugarOrigen() {
		return lugarOrigen;
	}
	public void setLugarOrigen(Lugares lugarOrigen) {
		this.lugarOrigen = lugarOrigen;
	}
	public Lugares getLugarDestino() {
		return lugarDestino;
	}
	public void setLugarDestino(Lugares lugarDestino) {
		this.lugarDestino = lugarDestino;
	}
	public Lugares getPuntosRecogida() {
		return puntosRecogida;
	}
	public void setPuntosRecogida(Lugares puntosRecogida) {
		this.puntosRecogida = puntosRecogida;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public User getConductor() {
		return conductor;
	}
	public void setConductor(User conductor) {
		this.conductor = conductor;
	}
	public String getCoche() {
		return coche;
	}
	public void setCoche(String coche) {
		this.coche = coche;
	}
	public short getPlazas() {
		return plazas;
	}
	public void setPlazas(short plazas) {
		this.plazas = plazas;
	}
	public boolean isVisibilidad() {
		return visibilidad;
	}
	public void setVisibilidad(boolean visibilidad) {
		this.visibilidad = visibilidad;
	}
	
	
	
}
