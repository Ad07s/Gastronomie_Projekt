FROM openjdk:26-ea-slim

WORKDIR /app

# JAR kopieren
COPY build/libs/*.jar app.jar

# Zertifikate in einen Unterordner kopieren
COPY src/main/resources/certificate.crt /app/config/certificate.crt
COPY src/main/resources/private-key.pem /app/config/private-key.pem

# Wir überschreiben die Pfade direkt beim Start.
# "file:" sorgt dafür, dass er NICHT in der JAR sucht.
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar", \
"--spring.ssl.bundle.pem.microservice.keystore.certificate=file:/app/config/certificate.crt", \
"--spring.ssl.bundle.pem.microservice.keystore.private-key=file:/app/config/private-key.pem", \
"--spring.ssl.bundle.pem.microservice.truststore.certificate=file:/app/config/certificate.crt"]