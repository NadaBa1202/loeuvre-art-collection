FROM node:22 AS front
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS back
WORKDIR /app
COPY backend/ ./backend/
COPY --from=front /app/frontend/dist/*/browser/ ./backend/src/main/resources/static/
RUN mvn -f backend/pom.xml -q -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=back /app/backend/target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-Xmx300m"
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]