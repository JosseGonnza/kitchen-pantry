<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=9df2ea&animation=fadeIn&height=120&section=header"/>

# 🧺 Kitchen Pantry
Kitchen Pantry es una aplicación pensada para ayudarte a organizar y controlar el inventario de una cocina de manera clara y sencilla. Su función principal es permitirte saber qué productos tienes, cuánto queda de cada uno y evitar que un día abras la despensa y descubras que te falta justo lo que necesitas. La idea es que puedas mantener todo ordenado, actualizado y listo para trabajar sin sorpresas.

Este proyecto está desarrollado en Java y utiliza una estructura bien organizada para que el código sea fácil de entender, mantener y ampliar. Aunque ahora está en desarrollo, la intención es convertirlo en una herramienta útil tanto para estudiantes como para cocineros.

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Building-C71A36?logo=apachemaven)
![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)
![Tests](https://img.shields.io/badge/Status-In%20progress-orange)

---

## 🚀 ¿Qué hace este proyecto?

Kitchen Pantry busca convertirse en un sistema capaz de gestionar de forma eficiente los productos de una cocina. A medida que avance el desarrollo, permitirá:

Registrar productos, sus categorías y sus unidades de medida (como gramos, litros, piezas, etc.).

Guardar entradas de productos (cuando compras o añades algo nuevo a la despensa) y salidas (cuando usas un ingrediente para cocinar).

Consultar el stock disponible en cualquier momento.

Enviar avisos internos cuando un producto esté cerca de agotarse, para que puedas reponerlo a tiempo.

Servir como base para construir una futura app web o incluso una herramienta de línea de comandos que permita interactuar con el inventario desde una terminal.

---

## 📦 Estructura del Proyecto

A continuación se muestra una visión general de cómo se organiza el proyecto. Cada carpeta tiene una responsabilidad concreta para mantener el orden y la claridad.

```
src/
└── main/
    ├── java/
    │   └── org/
    │       └── jossegonnza/
    │           └── kitchenpantry/
    │
    │               ├── api/
    │               │   ├── KitchenPantryApplication.java     # @SpringBootApplication
    │               │   ├── PantryConfig.java                  # @Configuration (beans Pantry, PantryService)
    │               │   │
    │               │   └── product/
    │               │       ├── ProductController.java         # /api/products
    │               │       └── dto/
    │               │           ├── CreateProductRequest.java
    │               │           └── ProductResponse.java
    │               │
    │               │   # Más adelante:
    │               │   #   batch/
    │               │   #   stock/
    │               │   #   etc.
    │
    │               ├── application/
    │               │   └── PantryService.java                 # Casos de uso del dominio
    │
    │               ├── domain/
    │               │   ├── Pantry.java
    │               │   ├── Product.java
    │               │   ├── Batch.java
    │               │   ├── Quantity.java
    │               │   ├── Category.java
    │               │   ├── StockSummary.java
    │               │   │
    │               │   └── exception/
    │               │       ├── DuplicateProductException.java
    │               │       ├── ProductNotFoundException.java
    │               │       └── InsufficientStockException.java
    │
    │               └── infrastructure/
    │                   └── jdbc/
    │                       └── PantryJdbcRepository.java       # Para el futuro con H2/Postgres
    │
    └── resources/
        └── application.yml                                     # Config de Spring, H2, perfiles…
```

Esta estructura ayuda a separar las diferentes tareas del sistema, evitando que todo quede mezclado y facilitando el trabajo en equipo o la ampliación del proyecto.

---

## 🧠 Arquitectura del Proyecto

La aplicación está diseñada usando un estilo de arquitectura por capas que permite organizar mejor las partes del programa. La idea es que cada capa tenga su propia responsabilidad y no interfiera con las demás.

```
[ REST Controllers ] → Reciben las peticiones del usuario
|
v
[ Command y Query Use Cases ] → Ejecutan la acción pedida
|
v
[ Domain Layer ] → Reglas del negocio y validaciones
|
v
[ Persistence Adapters ] → Guardan los datos en la base de datos
```

- Los controladores son quienes reciben las peticiones externas.
- Los casos de uso deciden qué debe pasar cuando llega una petición.
- El dominio contiene las reglas más importantes del sistema.
- Los adaptadores de persistencia conectan la lógica con la base de datos.
- Esta forma de organizar ayuda a que el proyecto sea fácil de probar, modificar y extender en el futuro.

---

## 🧪 Tests

```
mvn clean test
```

Las pruebas son una parte esencial del proyecto. Permiten comprobar que el sistema funciona como debería y que no se rompa nada cuando se hagan cambios. Los tests más importantes revisan:

- Que las cantidades y unidades se manejen correctamente.
- Que las reglas del inventario se cumplan en todos los casos.
- Que los eventos se activen cuando un producto queda por debajo del mínimo.

Esto garantiza que el proyecto sea confiable a medida que crece.

---

## ▶️ Ejecutar el backend

### Modo desarrollo (H2 en memoria)
```bash
mvn spring-boot:run
```
Accede a H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:pantrydb`
- User: `sa`
- Password: (vacío)

### Modo desarrollo con PostgreSQL
```bash
# 1. Levantar PostgreSQL
docker-compose up -d

# 2. Ejecutar la aplicación
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run

# 3. Parar PostgreSQL cuando termines
docker-compose down
```

### Modo producción
```bash
# Con variables de entorno
export DATABASE_URL=jdbc:postgresql://localhost:5432/kitchen_pantry
export DATABASE_USER=postgres
export DATABASE_PASSWORD=tu_password_seguro
export SPRING_PROFILES_ACTIVE=prod

mvn spring-boot:run
```

### Verificar profile activo
```bash
# El log debe mostrar: "The following profiles are active: dev"
mvn spring-boot:run | grep "profiles are active"
```

---

## 🔧 Tecnologías Principales

- Java 21
- Spring Boot 3.5.4
- Spring Data JDBC
- JUnit5

---

![Author](https://img.shields.io/badge/Author-Jose%20Gonnza-beige)
<img src="https://raw.githubusercontent.com/matfantinel/matfantinel/master/waves.svg" width="100%" height="100">