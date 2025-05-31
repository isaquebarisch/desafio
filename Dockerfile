FROM eclipse-temurin:24-jdk as build
WORKDIR /workspace/app

# Garantir que os scripts tenham permissões de execução
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x ./mvnw

# Copiar o arquivo pom.xml e baixar as dependências primeiro para aproveitar o cache do Docker
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copiar o código fonte e construir o aplicativo
COPY src src
RUN ./mvnw package -DskipTests -Dfile.encoding=UTF-8 -Dmaven.compiler.fork=true

FROM eclipse-temurin:24-jre
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar

# Expor a porta 8080
EXPOSE 8080

# Executar a aplicação
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar"]
