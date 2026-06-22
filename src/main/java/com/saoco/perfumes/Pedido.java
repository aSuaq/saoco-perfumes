package com.saoco.perfumes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String contacto;

    @Column(nullable = false)
    private String direccion;

    private String casaApto;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String provincia;

    private String codigoPostal;

    @Column(nullable = false, length = 2000)
    private String productos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}