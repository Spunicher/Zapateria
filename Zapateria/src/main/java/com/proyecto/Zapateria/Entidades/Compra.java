package com.proyecto.Zapateria.Entidades;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Compra")
public class Compra {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCompra;
	
	private Date Fecha;
	private double Total;
	
	@JsonIgnore
	@OneToMany(mappedBy = "FkCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<DetalleCompra> FkCompra;

	

	public int getIdCompra() {
		return idCompra;
	}



	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}



	public Date getFecha() {
		return Fecha;
	}



	public void setFecha(Date fecha) {
		Fecha = fecha;
	}



	public double getTotal() {
		return Total;
	}



	public void setTotal(double total) {
		Total = total;
	}



	public List<DetalleCompra> getFkCompra() {
		return FkCompra;
	}



	public void setFkCompra(List<DetalleCompra> fkCompra) {
		FkCompra = fkCompra;
	}


	

	public Compra(int idCompra, Date fecha, double total) {
		this.idCompra = idCompra;
		Fecha = fecha;
		Total = total;
	}


	public Compra( Date fecha, double total) {
		Fecha = fecha;
		Total = total;
	}
	
	public Compra() {
		
	}
	
	
	
}
