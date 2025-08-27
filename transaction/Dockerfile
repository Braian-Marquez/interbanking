# Dockerfile config server
# Para generar el build del dockerfile, se necesita hacer build Dockerfile del commons

# Usa la imagen de la biblioteca commons que construiste anteriormente
FROM commons AS build


WORKDIR /app

# Copia el código fuente de tu microservicio
COPY . .

# Construye el microservicio
RUN mvn clean package -DskipTests

# Imagen final para ejecutar el microservicio
FROM openjdk:17-oracle

WORKDIR /app

# Copia el jar construido de la etapa anterior
COPY --from=build /app/target/transaction-1.0.0.jar ./transaction-1.0.0.jar

# Expone el puerto en el que el servicio se ejecuta dentro del contenedor
EXPOSE 8081

# Define las variables de entorno necesarias
ENV CONFIG_EUREKA=registry-discovery \
    CONFIG_SERVER=server-config \
    JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true"

# Ejecuta el microservicio
CMD ["java","-jar","transaction-1.0.0.jar"]