# interbanking
# 📌 API REST con Microservicios, Spring Boot y JWT

Esta API permite gestionar usuarios y posts mediante una arquitectura de **microservicios**, con autenticación basada en **JWT** y control de acceso. Se utiliza **Consul** para el registro de servicios y **Spring Cloud Gateway** como puerta de enlace de la API.  

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.1.2**
- **Spring Security + JWT** (Autenticación)
- **Spring Cloud Gateway** (Gestión de tráfico y seguridad)
- **Spring Cloud Consul** (Registro y descubrimiento de servicios)
- **JPA + Hibernate** (Persistencia)
- **PostgreSQL** (Base de datos)
- **Lombok** (Para reducir código boilerplate)
- **Docker & Portainer** (Gestión de contenedores)
- **Hostinger** (Infraestructura en la nube)
- **Nginx** (Proxy inverso)
- **Postman** (Pruebas y documentación)

---
## 📂Documentacion de Postman
https://documenter.getpostman.com/view/21902697/2sB34ZrjZ8

## 📂 Arquitectura del Proyecto

Este sistema sigue un enfoque basado en **microservicios**, donde cada módulo cumple una función específica.

```plaintext
├── api-gateway (Spring Cloud Gateway)
├── api-auth (Gestión de autenticación y JWT)
├── api-transaction (Gestión de Transacciones)
├── Consul (Registro de servicios y configuración dinámica)
└── Nginx (Proxy inverso)
```

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

## 1️⃣ Levantar Consul
Consul se ejecuta en el puerto 8500.
Se debe crear una red en Docker llamada app-network

```bash
docker network create app-network
```

Iniciar Consul con Docker desde la consola

```bash
docker run -d --name=consul `
  --network=app-network `
  -p 8500:8500 `
  -p 8600:8600/udp `
  -e CONSUL_BIND_INTERFACE=eth0 `
  hashicorp/consul:latest agent -server -bootstrap-expect=1 --client 0.0.0.0 --ui

```
O a traves de un docker-compose.yml o un stack en Portainer

```yaml
services:
  consul:
    image: hashicorp/consul:latest
    container_name: consul
    command: agent -server -bootstrap-expect=1 -client=0.0.0.0 -ui
    ports:
      - "8500:8500"
      - "8600:8600/udp"
    environment:
      - CONSUL_BIND_INTERFACE=eth0
    volumes:
      - consul-data:/consul/data
    networks:
      - app-network

volumes:
  consul-data:
    driver: local

networks:
  app-network:
    external: true
```
## 2️⃣ Agregar la Configuración en Consul
Para que los servicios api-auth y api-transaction obtengan su configuración desde Consul, debes crear las claves en el KV Store.

## 📌 Registrar Configuración en Consul en Windows
Ejecuta los siguientes comandos para registrar la configuración en Consul KV:
Constatar de que en consul el formato de los arhivos que se crean sean YAML
### Aclaracion las credenciales de base de datos puestas son solo un ejemplo.
```yaml
$yamlConfig = @"
spring:
  application:
    name: api-auth  
  datasource:
    url: jdbc:postgresql://52.207.27.199:5432/prueba_tecnica
    username: postgres
    password: Pa55w0rd
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
auth:
  security:
    SECRET_KEY: "6B32794D4F6A396A5231552A58356B744C687654527A5670336B397839274D"
"@

Invoke-RestMethod -Uri "http://localhost:8500/v1/kv/config/api-auth/data" -Method Put -Body $yamlConfig -ContentType "text/plain"

```

```yaml
$yamlConfig = @"
spring:
  application:
    name: api-transaction  
  datasource:
    url: jdbc:postgresql://52.207.27.199:5432/prueba_tecnica
    username: postgres
    password: Pa55w0rd
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
auth:
  security:
    SECRET_KEY: "6B32794D4F6A396A5231552A58356B744C687654527A5670336B397839274D"
"@

Invoke-RestMethod -Uri "http://localhost:8500/v1/kv/config/api-transaction/data" -Method Put -Body $yamlConfig -ContentType "text/plain"

```

## 🚀 Levantar Microservicios 

```bash
git clone https://github.com/Braian-Marquez/interbanking
```
### 1️⃣ Construir el servicio **commons**  
Antes de iniciar los microservicios, es necesario compilar y construir el módulo **commons**, ya que proporciona recursos compartidos para los demás servicios.  

Ejecuta el siguiente comando dentro del directorio del microservicio **commons**:  

```bash
mvn clean install
```
Levantar el API Gateway

```bash
cd gateway
./mvnw spring-boot:run
```
Levantar api-auth

```bash
cd ../api-auth
./mvnw spring-boot:run
```
Levantar api-transaction

```bash
cd ../api-transaction
./mvnw spring-boot:run
```

## 2️⃣ Levantar los Microservicios - Docker (Estos pasos no son necesarios para correr el proyecto mas que nada es a nivel informativo de como esta funcionando actualmente la arquitectura)

```yaml
services:

  api-auth:
    image: braianm95/api-auth
    container_name: api-auth
    ports:
      - "8081:8081"
    environment:
      - CONSUL_HOST=consul
      - CONSUL_PORT=8500
    depends_on:
      - api-gateway
    networks:
      - app-network

  api-transaction:
    image: braianm95/api-transaction
    container_name: api-transaction
    ports:
      - "8082:8082"
    environment:
      - CONSUL_HOST=consul
      - CONSUL_PORT=8500
      - SPRING_APPLICATION_NAME=api-transaction
    depends_on:
      - api-gateway
    networks:
      - app-network
      
  api-gateway:
    image: braianm95/api-gateway
    container_name: api-gateway
    ports:
      - "8080:8080"
    environment:
      - CONSUL_HOST=consul
      - CONSUL_PORT=8500
    networks:
      - app-network

networks:
  app-network:
    external: true
```

