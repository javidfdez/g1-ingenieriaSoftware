package ingsoftware.fuengiuma.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
	private int id;

<<<<<<< HEAD

	@DateTimeFormat(iso = ISO.DATE)
=======
	
    @DateTimeFormat(iso = ISO.DATE)
>>>>>>> refs/remotes/origin/main
	private Date HoraSalida;
	@DateTimeFormat(iso = ISO.DATE)
	private Date HoraLlegada;
	private double precio;
<<<<<<< HEAD
	private String coche;
	private short plazas;
	private boolean visibilidad;

	@ManyToMany(mappedBy = "viajesOrigen")
	private List<Lugares> puntosRecogida;

	@ManyToMany(mappedBy = "viajesDestino")
	private List<Lugares> destino;

	@ManyToMany
	private List<Usuario> pasajeros;

	@ManyToOne
	private Usuario conductor;


	public Viaje(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
=======
	private String coche;    
	private short plazas; 
	private boolean visibilidad;
	
	@ManyToMany (mappedBy="viajesOrigen")
	private List<Lugares> puntosRecogida;
	
	@ManyToMany (mappedBy="viajesDestino")
	private List<Lugares> destino;
	
	@ManyToMany
	private List<User> pasajeros;
	
	@ManyToOne 
	private User conductor;

	public List<Lugares> getOrigen() {
		return origen;
	}

	public void setOrigen(List<Lugares> origen) {
		this.origen = origen;
	}

	public List<Lugares> getDestino() {
		return destino;
	}

	public void setDestino(List<Lugares> destino) {
		this.destino = destino;
	}

	public List<User> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<User> pasajeros) {
		this.pasajeros = pasajeros;
	}

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
>>>>>>> refs/remotes/origin/main
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
<<<<<<< HEAD
=======
	}
	public User getConductor() {
		return conductor;
	}
	public void setConductor(User conductor) {
		this.conductor = conductor;
>>>>>>> refs/remotes/origin/main
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
<<<<<<< HEAD
	}

	public List<Lugares> getPuntosRecogida() {
		return puntosRecogida;
	}

	public void setPuntosRecogida(List<Lugares> puntosRecogida) {
		this.puntosRecogida = puntosRecogida;
	}

	public List<Lugares> getDestino() {
		return destino;
	}

	public void setDestino(List<Lugares> destino) {
		this.destino = destino;
	}

	public List<Usuario> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<Usuario> pasajeros) {
		this.pasajeros = pasajeros;
	}

	public Usuario getConductor() {
		return conductor;
	}

	public void setConductor(Usuario conductor) {
		this.conductor = conductor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Viaje viaje = (Viaje) o;
		return id == viaje.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Viaje{" +
				"id=" + id +
				", HoraSalida=" + HoraSalida +
				", HoraLlegada=" + HoraLlegada +
				", precio=" + precio +
				", coche='" + coche + '\'' +
				", plazas=" + plazas +
				", visibilidad=" + visibilidad +
				", puntosRecogida=" + puntosRecogida +
				", destino=" + destino +
				", pasajeros=" + pasajeros +
				", conductor=" + conductor +
				'}';
=======
>>>>>>> refs/remotes/origin/main
	}
}
