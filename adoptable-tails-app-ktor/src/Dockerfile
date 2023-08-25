FROM arm64v8/eclipse-temurin:17-jre-ubi9-minimal

COPY build/libs/adoptable-tails-app-ktor-all.jar /app/app.jar
EXPOSE 8080

CMD ["java", "-XX:+UseContainerSupport", "-Dfile.encoding=UTF8", "-jar", "/app/app.jar"]