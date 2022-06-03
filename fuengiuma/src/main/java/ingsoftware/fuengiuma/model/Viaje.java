package ingsoftware.fuengiuma.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Viaje {

	@Id
	@GeneratedValue
	private Integer id;


	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	private Date horaSalida;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	private Date horaLlegada;
	private double precio;
	private String coche;
	private short plazas;
	private boolean visibilidad;
	private String origen;
	private String destino;

//	@ManyToOne
//	private Usuario conductor;

	public Viaje(){

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(Date horaSalida) {
		this.horaSalida = horaSalida;
	}

	public Date getHoraLlegada() {
		return horaLlegada;
	}

	public void setHoraLlegada(Date horaLlegada) {
		this.horaLlegada = horaLlegada;
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

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

//	public Usuario getConductor() {
//		return conductor;
//	}
//
//	public void setConductor(Usuario conductor) {
//		this.conductor = conductor;
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Viaje viaje = (Viaje) o;

		return id.equals(viaje.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Viaje{" +
				"id=" + id +
				", HoraSalida=" + horaSalida +
				", HoraLlegada=" + horaLlegada +
				", precio=" + precio +
				", coche='" + coche + '\'' +
				", plazas=" + plazas +
				", visibilidad=" + visibilidad +
				", origen='" + origen + '\'' +
				", destino='" + destino + '\'' +
				'}';
	}
}
