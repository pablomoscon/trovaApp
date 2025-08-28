# Trova App - Backend

Este es el **backend de Trova App**, una aplicación web para explorar los álbumes y artistas del sello discográfico **Trova**.  

Permite acceder a información detallada de artistas y discos, escuchar música en plataformas externas y administrar usuarios, artistas y álbumes desde un panel administrativo.  

El proyecto está desarrollado con **Spring Boot** y expone una **API REST** segura y documentada, con persistencia en **PostgreSQL** y autenticación basada en **JWT**.

---

## Características principales

- 🔐 **Autenticación y autorización** con JWT (roles: usuario / administrador)  
- 👤 **Gestión de usuarios** (registro, login, administración solo para admin)  
- 🎤 **Módulo de artistas** (CRUD completo + detalles)  
- 💿 **Módulo de álbumes** (CRUD completo + relación con artistas)  
- ☁️ **Almacenamiento en AWS S3** para portadas de álbumes  
- 📊 **Reportes y estadísticas** de visitas (solo admin)  
- 📖 **Documentación con Swagger / OpenAPI**  

---

## Tecnologías principales

- **Java 17**
- **Spring Boot 3.2**
  - Spring Web (REST API)
  - Spring Data JPA (ORM)
  - Spring Security (JWT)
  - Spring Validation
- **PostgreSQL** (base de datos principal)
- **H2** (para testing)
- **Swagger / OpenAPI** (documentación)
- **AWS SDK (S3)** (archivos multimedia)
- **Mockito + JUnit 5** (tests)
- **Dotenv** (variables de entorno)

---

## Estructura del proyecto

```bash
trovaApp/
├── src/main/java/com/trovaDisc/
│   ├── controller/     # Controladores REST
│   ├── service/        # Lógica de negocio
│   ├── repository/     # Repositorios JPA
│   ├── model/          # Entidades (JPA)
│   └── security/       # Configuración y filtros JWT
│
├── src/main/resources/
│   ├── application.properties       # Configuración base
│   └── application-dev.properties   # Configuración dev
│   └── application-prod.properties   # Configuración prod
│
└── src/test/java/       # Tests unitarios e integración
