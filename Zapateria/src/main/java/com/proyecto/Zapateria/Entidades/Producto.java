package com.proyecto.Zapateria.Entidades;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "Producto")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idProducto;
	private String nombre;
	private String Precio;
	private String imagen;
	private int estado;
	private String marca;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "FkProducto", cascade = CascadeType.ALL)
	public List<DetalleCompra> FkProducto;
	
	@JsonIgnore
	@OneToMany(mappedBy = "FkProducto2", cascade = CascadeType.ALL)
	public List<Carrito> FkProducto2;

		public int getIdProducto() {
		return idProducto;
	}




	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}




	public String getNombre() {
		return nombre;
	}




	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	public String getPrecio() {
		return Precio;
	}




	public void setPrecio(String precio) {
		Precio = precio;
	}




	public String getImagen() {
		return imagen;
	}




	public void setImagen(String imagen) {
		this.imagen = imagen;
	}




	public int getEstado() {
		return estado;
	}




	public void setEstado(int estado) {
		this.estado = estado;
	}




	public String getMarca() {
		return marca;
	}




	public void setMarca(String marca) {
		this.marca = marca;
	}




		public Producto(int idProducto, String nombre, String precio, String imagen, int estado, String marca) {
		this.idProducto = idProducto;
		this.nombre = nombre;
		Precio = precio;
		this.imagen = imagen;
		this.estado = estado;
		this.marca = marca;
	}

		public Producto( String nombre, String precio, String imagen, int estado, String marca) {
			this.nombre = nombre;
			Precio = precio;
			this.imagen = imagen;
			this.estado = estado;
			this.marca = marca;
		}


		public Producto() {
		}
	
		
}
