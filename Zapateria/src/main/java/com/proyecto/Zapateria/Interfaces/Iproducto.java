package com.proyecto.Zapateria.Interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Zapateria.Entidades.Producto;

@Repository
public interface Iproducto extends JpaRepository<Producto, Integer> {

}
