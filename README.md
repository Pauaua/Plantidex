# CTRL - Sistema de Gestión de Usuarios

## Descripción
CTRL es una aplicación web desarrollada con Spring Boot que proporciona un sistema de gestión de usuarios con roles y permisos. Permite el registro de usuarios, autenticación, y administración de perfiles con diferentes niveles de acceso.

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/ctrl/home/
│   │       ├── controllers/
│   │       │   ├── AppController.java      # Controlador principal de la aplicación
│   │       │   └── UsuarioController.java  # Controlador para gestión de usuarios
│   │       ├── models/
│   │       │   └── Usuario.java            # Modelo de datos de Usuario
│   │       └── repositories/
│   │           └── UsuarioRepository.java   # Repositorio para operaciones con la BD
│   └── resources/
│       ├── static/
│       │   └── css/
│       │       └── styles.css              # Estilos de la aplicación
│       ├── templates/                      # Plantillas Thymeleaf
│       │   ├── acceso-denegado.html
│       │   ├── crear-usuario.html
│       │   ├── dashboard.html
│       │   ├── detalle-usuario.html
│       │   ├── editar-usuario.html
│       │   ├── index.html
│       │   ├── login.html
│       │   ├── registro.html
│       │   └── usuarios.html
│       └── application.properties          # Configuración de la aplicación
```

## Características Principales

### Gestión de Usuarios
- Registro de nuevos usuarios
- Inicio de sesión
- Visualización y edición de perfiles
- Gestión de roles (ADMIN/USER)
- Estado de usuarios (ACTIVO/INACTIVO)

### Roles y Permisos
- **ADMIN**: Acceso completo al sistema
  - Gestión de todos los usuarios
  - Creación/Edición/Eliminación de usuarios
  - Vista de lista completa de usuarios
- **USER**: Acceso limitado
  - Vista de su propio perfil
  - Edición de datos personales

## Tecnologías Utilizadas

- Java 25
- Spring Boot
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven
- HTML5/CSS3

## Configuración del Proyecto

### Requisitos Previos
- Java SE Development Kit 25 o superior
- Maven 3.6.x o superior

### Pasos de Instalación

1. Clonar el repositorio:
```bash
git clone [URL-del-repositorio]
```

2. Navegar al directorio del proyecto:
```bash
cd CTRL
```

3. Compilar el proyecto:
```bash
mvn clean install
```

4. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### Configuración de Base de Datos

La aplicación utiliza H2 Database por defecto. La configuración se encuentra en `application.properties`:

```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:file:./data/ctrl_db
spring.jpa.hibernate.ddl-auto=update
```

## Funcionalidades por Vista

### Login (`login.html`)
- Formulario de inicio de sesión
- Validación de credenciales
- Redirección según rol de usuario

### Registro (`registro.html`)
- Formulario de registro de nuevos usuarios
- Validación de campos
- Asignación automática de roles

### Dashboard (`dashboard.html`)
- Vista principal después del login
- Información personalizada según rol

### Gestión de Usuarios (`usuarios.html`)
- Lista de usuarios (solo ADMIN)
- Opciones de gestión
- Filtros y búsqueda

## Seguridad

- Validación de sesiones
- Protección de rutas según roles
- Validación de formularios
- Gestión de accesos no autorizados

## Mantenimiento

### Logs
La aplicación registra eventos importantes para facilitar el mantenimiento y la depuración.

### Backup
Se recomienda realizar backups periódicos de la base de datos H2.

## Contacto

Para más información o reportar problemas, contactar al equipo de desarrollo.

---

## Informe PDF

[Informe PDF Evaluación I](https://github.com/user-attachments/files/23347007/EV1_DSII_acuna_paulina.pdf)