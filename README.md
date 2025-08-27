# interbanking
# 📌 API REST con Microservicios, Arquitectura Hexagonal y JWT

Esta API permite gestionar usuarios y transacciones mediante una **arquitectura de microservicios** basada en **arquitectura hexagonal**, con autenticación basada en **JWT** y control de acceso. Se utiliza **Eureka Server** para el registro de servicios y **Spring Cloud Gateway** como puerta de enlace de la API.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.8**
- **Arquitectura Hexagonal** (Clean Architecture)
- **Spring Security + JWT** (Autenticación)
- **Spring Cloud Gateway** (Gestión de tráfico y seguridad)
- **Eureka Server** (Registro y descubrimiento de servicios)
- **JPA + Hibernate** (Persistencia)
- **PostgreSQL** (Base de datos)
- **Lombok** (Para reducir código boilerplate)
- **JUnit 5 + Mockito** (Testing unitario)
- **OpenAPI 3 (Swagger)** (Documentación de APIs)
- **Docker** (Gestión de contenedores)

---
## 📂Documentacion de Postman
https://documenter.getpostman.com/view/21902697/2sB34ZrjZ8

## 📂 Arquitectura del Proyecto

Este sistema sigue un enfoque basado en **microservicios** con **arquitectura hexagonal**, donde cada módulo cumple una función específica y mantiene una separación clara entre la lógica de negocio y la infraestructura.

```plaintext
├── gateway (Spring Cloud Gateway)
├── autentication (Gestión de autenticación y JWT con Arquitectura Hexagonal)
├── transaction (Gestión de Transacciones con Arquitectura Hexagonal)
├── eureka (Registro y descubrimiento de servicios)
└── commons (Entidades y utilidades compartidas)
```

### 🏗️ Arquitectura Hexagonal

Cada microservicio está estructurado siguiendo los principios de la arquitectura hexagonal:

- **Domain**: Contiene la lógica de negocio pura y los puertos (interfaces)
- **Application**: Casos de uso y servicios de aplicación
- **Infrastructure**: Adaptadores para bases de datos, APIs externas y configuración

## 🔄 Pasos para Iniciar el Proyecto

## 2️⃣ Requisitos previos
Antes de iniciar, asegúrate de tener instalado lo siguiente:

## ✅ Docker
## ✅ Git
## ✅ Java 17+
## ✅ Maven o Gradle

### 1️⃣ Clonar el repositorio

```bash
git clone <URL_DEL_REPO>
cd <NOMBRE_DEL_PROYECTO>
```

## 1️⃣ Configurar los servicios de infraestructura

Los archivos de configuración para cada servicio ya están incluidos en sus respectivos directorios `src/main/resources/`.

## 🚀 Levantar Microservicios 

```bash
git clone https://github.com/Braian-Marquez/interbanking
```
### 1️⃣ Construir el servicio **commons**  
Antes de iniciar los microservicios, es necesario compilar y construir el módulo **commons**, ya que proporciona recursos compartidos para los demás servicios.  

```bash
cd commons
mvn clean install
```

### 2️⃣ Iniciar servicios en orden

⚠️ **IMPORTANTE**: Los servicios deben iniciarse en el siguiente orden para garantizar el correcto funcionamiento del sistema:

**1. Eureka Server:**
```bash
cd eureka
./mvnw spring-boot:run
```
*Espera a que Eureka esté completamente iniciado antes de continuar*

**2. Gateway:**
```bash
cd ../gateway
./mvnw spring-boot:run
```
*Espera a que el Gateway se registre en Eureka*

**3. Authentication Service:**
```bash
cd ../autentication
./mvnw spring-boot:run
```

**4. Transaction Service:**
```bash
cd ../transaction
./mvnw spring-boot:run
```

## 📋 Puertos de los Servicios

- **Eureka Server**: http://localhost:8761
- **Gateway**: http://localhost:8080
- **Authentication Service**: http://localhost:8081
- **Transaction Service**: http://localhost:8082

## 📚 Documentación de APIs (Swagger)

Una vez que todos los servicios estén ejecutándose, puedes acceder a la documentación interactiva de las APIs:

🌐 **Panel Principal de Swagger**: http://localhost:8080

Desde el gateway podrás acceder a:
- **Authentication API**: Endpoints para registro, login y gestión de usuarios
- **Transaction API**: Endpoints para gestión de empresas y transferencias

La documentación incluye ejemplos de requests, respuestas y permite probar los endpoints directamente desde el navegador.

## 🧪 Testing

El proyecto incluye **testing unitario** para todos los servicios principales:

### Ejecutar tests del servicio Authentication:
```bash
cd autentication
./mvnw test
```

### Ejecutar tests del servicio Transaction:
```bash
cd transaction
./mvnw test
```

Los tests incluyen:
- **UserServiceTest**: Tests unitarios para funcionalidades de autenticación
- **TransactionServiceTest**: Tests unitarios para operaciones de transferencias

## 🔧 Configuración

La configuración de cada servicio se maneja mediante:
- **Archivos locales**: `application.properties` en cada servicio
- **Eureka**: Para el registro automático de servicios
- **Arquitectura Hexagonal**: Separación clara entre capas de dominio, aplicación e infraestructura

