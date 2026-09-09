# -------------------------------------------------------------
# 1. BUILD STAGE: Compila o projeto e gera o arquivo .jar
# -------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia as configurações do Maven e o código-fonte
COPY pom.xml .
COPY src ./src

# Compila e gera o pacote pulando os testes (que já rodamos na Etapa 5)
RUN mvn clean package -DskipTests

# -------------------------------------------------------------
# 2. RUN STAGE: Imagem final enxuta apenas para execução
# -------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]