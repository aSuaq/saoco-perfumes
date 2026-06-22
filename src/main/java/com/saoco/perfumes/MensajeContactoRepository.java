package com.saoco.perfumes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {
    List<MensajeContacto> findAllByOrderByFechaDesc();
    List<MensajeContacto> findByLeidoFalse();
    long countByLeidoFalse();
}