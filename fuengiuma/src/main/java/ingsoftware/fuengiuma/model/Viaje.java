package ingsoftware.fuengiuma.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Viaje {

	@Id
	@GeneratedValue
	private int Código;
	
	private Lugares LugarOrigen;
	private Lugares LugarDestino;
	private Lugares PuntosRecogida;
	
    @DateTimeFormat(iso = ISO.DATE)
	private Date HoraSalida;
	private Date HoraLlegada;   
	private double Precio;  
	private User Conductor;   
	private String Coche;    
	private short Plazas; 
	private boolean Visibilidad;
	
	@ManyToMany (mappedBy="viajesOrigen")
	private List<Lugares> origen;
	
	@ManyToMany (mappedBy="viajesDestino")
	private List<Lugares> destino;
	
	
	
	
	public Viaje(int código, Lugares lugarOrigen, Lugares lugarDestino, Lugares puntosRecogida, Date horaSalida,
			Date horaLlegada, double precio, User conductor, String coche, short plazas, boolean visibilidad) {
		Código = código;
		LugarOrigen = lugarOrigen;
		LugarDestino = lugarDestino;
		PuntosRecogida = puntosRecogida;
		HoraSalida = horaSalida;
		HoraLlegada = horaLlegada;
		Precio = precio;
		Conductor = conductor;
		Coche = coche;
		Plazas = plazas;
		Visibilidad = visibilidad;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Código;
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
		if (Código != other.Código)
			return false;
		return true;
	}
	public int getCódigo() {
		return Código;
	}
	public void setCódigo(int código) {
		Código = código;
	}
	public Lugares getLugarOrigen() {
		return LugarOrigen;
	}
	public void setLugarOrigen(Lugares lugarOrigen) {
		LugarOrigen = lugarOrigen;
	}
	public Lugares getLugarDestino() {
		return LugarDestino;
	}
	public void setLugarDestino(Lugares lugarDestino) {
		LugarDestino = lugarDestino;
	}
	public Lugares getPuntosRecogida() {
		return PuntosRecogida;
	}
	public void setPuntosRecogida(Lugares puntosRecogida) {
		PuntosRecogida = puntosRecogida;
	}
	public Date getHoraSalida() {
		return HoraSalida;
	}
	public void setHoraSalida(Date horaSalida) {
		HoraSalida = horaSalida;
	}
	public Date getHoraLlegada() {
		return HoraLlegada;
	}
	public void setHoraLlegada(Date horaLlegada) {
		HoraLlegada = horaLlegada;
	}
	public double getPrecio() {
		return Precio;
	}
	public void setPrecio(double precio) {
		Precio = precio;
	}
	public User getConductor() {
		return Conductor;
	}
	public void setConductor(User conductor) {
		Conductor = conductor;
	}
	public String getCoche() {
		return Coche;
	}
	public void setCoche(String coche) {
		Coche = coche;
	}
	public short getPlazas() {
		return Plazas;
	}
	public void setPlazas(short plazas) {
		Plazas = plazas;
	}
	public boolean isVisibilidad() {
		return Visibilidad;
	}
	public void setVisibilidad(boolean visibilidad) {
		Visibilidad = visibilidad;
	}
	
	
	
}
