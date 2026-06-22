package com.saoco.perfumes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_contacto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String contacto;

    @Column(nullable = false, length = 2000)
    private String mensaje;

    @Column(nullable = false)
    private boolean leido = false;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}