package com.saoco.perfumes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByDisponibleTrue();
    
    // Paginación para el catálogo (solo productos disponibles)
    Page<Producto> findByDisponibleTrue(Pageable pageable);
    
    // Paginación por categoría
    Page<Producto> findByCategoria(String categoria, Pageable pageable);
}