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

@Entity
public class Viaje {

	@Id
	@GeneratedValue
	private Integer id;


	@DateTimeFormat(iso = ISO.DATE)
	private Date HoraSalida;
	@DateTimeFormat(iso = ISO.DATE)
	private Date HoraLlegada;
	private double precio;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
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
	}
}
