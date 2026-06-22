package com.saoco.perfumes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class SaocoPerfumesApp {

    public static void main(String[] args) {
        SpringApplication.run(SaocoPerfumesApp.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(ProductoRepository productoRepo) {
        return args -> {
            if (productoRepo.count() == 0) {
                List<Producto> productos = List.of(
                    new Producto(null, "Armaf Odyssey Mandarin Sky", "ARMAF", "Cítricos", 
                        "Mandarina, bergamota, jengibre", 
                        new BigDecimal("180000"), true, 15,
                        "https://kimi-web-img.moonshot.cn/img/armaf.com/55c6d8a3932553179c07d3771c8342021b17f6e2.jpg"),
                    new Producto(null, "Lattafa Khamrah", "LATTAFA", "Dulces", 
                        "Canela, nuez moscada, vainilla", 
                        new BigDecimal("180000"), true, 20,
                        "https://kimi-web-img.moonshot.cn/img/armaf.com/55c6d8a3932553179c07d3771c8342021b17f6e2.jpg"),
                    new Producto(null, "Afnan 9PM Night Out", "AFNAN", "Árabes", 
                        "Manzana, canela, vainilla", 
                        new BigDecimal("250000"), true, 8,
                        "https://kimi-web-img.moonshot.cn/img/fimgs.net/ad9240e2243080f4b557147d45371db208d717ee.jpg"),
                    new Producto(null, "Rasasi Hawas Fire", "RASASI", "Árabes", 
                        "Pimienta rosa, canela, incienso", 
                        new BigDecimal("350000"), true, 5,
                        "https://kimi-web-img.moonshot.cn/img/rasasistore.com/31147a93d21db46997c9eeca715070a681b704b6.jpg")
                );
                productoRepo.saveAll(productos);
                System.out.println("✓ Productos precargados");
            }
        };
    }


}