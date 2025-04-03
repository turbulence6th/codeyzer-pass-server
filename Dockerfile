# OpenJDK 17 image'i kullanıyoruz (versiyonu uygulamanın ihtiyaçlarına göre değiştirebilirsiniz)
FROM openjdk:17-jdk-slim

# Uygulamanın jar dosyasını container'a kopyalayacağız
COPY target/pass-server-0.0.1-SNAPSHOT.jar pass-server-0.0.1-SNAPSHOT.jar

# Uygulamanın hangi portta çalıştığını belirt
EXPOSE 8080

# Uygulamayı çalıştıran komut
ENTRYPOINT ["java", "-jar", "/pass-server-0.0.1-SNAPSHOT.jar"]