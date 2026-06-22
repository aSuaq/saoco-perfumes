# Saoco Perfumes — Sitio web (Spring Boot + MySQL)

Sitio web de una sola página para **Saoco Perfumes** (perfumería árabe, Bogotá | Cúcuta).
El catálogo de productos se guarda en **MySQL** y se carga con Spring Data JPA. El front es HTML + CSS + JavaScript con Thymeleaf, sin frameworks de JS.

Proyecto simple a propósito: 4 clases Java en total.

## Funcionalidades

- **Inicio**: hero con la identidad de la marca.
- **Catálogo**: productos traídos desde MySQL, con filtro por categoría y botón a WhatsApp por producto.
- **Contacto**: formulario que muestra una confirmación (por ahora no se guarda en BD; ver "Próximos pasos").
- Al arrancar la app por primera vez, si la tabla `productos` está vacía, se precarga un catálogo de ejemplo.

## Requisitos

- Java 17+
- Maven 3.9+
- MySQL 8 corriendo localmente

## 1. Configurar la base de datos

No es necesario crearla a mano: `application.properties` tiene `createDatabaseIfNotExist=true`, MySQL la crea sola. Si prefieres crearla tú:

```sql
CREATE DATABASE saoco_db;
```

## 2. Configurar credenciales

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/saoco_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Bogota
spring.datasource.username=root
spring.datasource.password=tu_password_aqui
```

## 3. Ejecutar

```bash
mvn spring-boot:run
```

Abre **http://localhost:8080**.

## 4. Cambiar el número de WhatsApp

Reemplaza `573000000000` por el número real en `src/main/resources/templates/index.html` (aparece 3 veces).

## Estructura del proyecto

```
saoco-perfumes/
├── pom.xml
├── src/main/java/com/saoco/perfumes/
│   ├── SaocoPerfumesApplication.java   # arranca Spring Boot + precarga productos de ejemplo
│   ├── Producto.java                   # entidad JPA -> tabla "productos"
│   ├── ProductoRepository.java         # consultas a la base de datos
│   └── HomeController.java             # sirve la página y procesa el formulario de contacto
└── src/main/resources/
    ├── application.properties          # conexión a MySQL
    ├── templates/index.html
    └── static/
        ├── css/style.css
        └── js/script.js
```

## Agregar productos reales

Inserta directamente en MySQL, no hace falta tocar código:

```sql
INSERT INTO productos (nombre, marca, categoria, notas, precio_cop, disponible)
VALUES ('Bade''e Al Oud', 'Lattafa', 'Arabe', 'Oud, rosa, azafrán', 195000, true);
```

Categorías que reconoce el filtro (respeta mayúsculas/minúsculas): `Arabe`, `Amaderado`, `Dulce`, `Floral`, `Citrico`.

## Próximos pasos (no incluidos todavía)

Cuando lo necesites, esto se puede agregar sin reescribir lo que ya hay:

- Guardar los mensajes de contacto en una tabla `contactos`.
- Login / autenticación para un panel de administración.
- Panel de admin para agregar productos y ver ventas desde el navegador (sin tocar SQL).
- Desplegar en GitHub / un hosting (Render, Railway, etc.).
