package com.proyecto.Zapateria.Interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Zapateria.Entidades.Compra;

@Repository
public interface Icompra extends JpaRepository<Compra, Integer>{

}
