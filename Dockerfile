# Базовый образ с JRE (для запуска)
FROM eclipse-temurin:21-jre

# (опционально) рабочая папка внутри контейнера
WORKDIR /app

# копируем jar в контейнер
# предполагаем, что jar лежит в target/app.jar рядом с Dockerfile
COPY target/*.jar app.jar

# (опционально) порт, который приложение слушает внутри контейнера
EXPOSE 8080

# команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]