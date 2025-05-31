# API de Gerenciamento de Dispositivos (Devices API)

Este projeto implementa uma API RESTful para gerenciamento de recursos de dispositivos (Devices) com operações completas de CRUD e validações de domínio específicas.

## Funcionalidades Suportadas

* Criação de novos dispositivos
* Atualização completa ou parcial de dispositivos existentes
* Busca de dispositivo por ID
* Listagem de todos os dispositivos
* Filtragem de dispositivos por marca
* Filtragem de dispositivos por estado
* Exclusão de dispositivos

## Regras de Negócio

* Tempo de criação não pode ser alterado
* Nome e marca não podem ser atualizados se o dispositivo estiver em uso
* Dispositivos em uso não podem ser excluídos

## Requisitos

* Java 21+
* Maven 3.9+ ou Gradle 8+ 
* Docker e Docker Compose

## Tecnologias Utilizadas

* Spring Boot 3.5.0
* Spring Data JPA
* PostgreSQL
* Docker
* Swagger/OpenAPI para documentação
* JUnit 5 + TestContainers para testes

## Como Executar

### Usando Docker Compose (Recomendado)

1. Clone este repositório:
```bash
git clone <url-do-repositorio>
cd <nome-do-repositorio>
```

2. Inicie a aplicação com Docker Compose:
```bash
docker-compose up
```

3. Acesse a API em: [http://localhost:8080/api/devices](http://localhost:8080/api/devices)

### Localmente

1. Certifique-se de ter o PostgreSQL em execução e configure as credenciais no arquivo `application.properties`

2. Execute o projeto usando Maven:
```bash
./mvnw spring-boot:run
```

## Documentação da API

A documentação interativa da API está disponível via Swagger UI após iniciar a aplicação:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Endpoints Disponíveis

### Criar um Dispositivo
```
POST /api/devices
```

### Obter Dispositivo por ID
```
GET /api/devices/{id}
```

### Listar Todos os Dispositivos
```
GET /api/devices
```

### Listar Dispositivos por Marca
```
GET /api/devices/brand/{brand}
```

### Listar Dispositivos por Estado
```
GET /api/devices/state/{state}
```
Estados disponíveis: `AVAILABLE`, `IN_USE`, `INACTIVE`

### Atualizar um Dispositivo (Completo)
```
PUT /api/devices/{id}
```

### Atualizar um Dispositivo (Parcial)
```
PATCH /api/devices/{id}
```

### Excluir um Dispositivo
```
DELETE /api/devices/{id}
```

## Testes

O projeto inclui testes unitários e de integração. Para executar todos os testes:

```bash
./mvnw test
```

## Possíveis Melhorias Futuras

1. Implementar segurança com autenticação e autorização (OAuth2, JWT)
2. Adicionar paginação e ordenação na listagem de dispositivos
3. Implementar cache para melhorar o desempenho
4. Adicionar monitoramento e métricas com Prometheus/Grafana
5. Implementar versionamento da API
6. Adicionar logs mais detalhados e rastreamento de requisições

## Estrutura do Projeto

O projeto segue o padrão de arquitetura MVC (Model-View-Controller):

- **Model**: Entidade `Device` que representa o modelo de dados
- **Controller**: `DeviceController` que expõe os endpoints REST
- **Service**: `DeviceService` contendo a lógica de negócios
- **Repository**: `DeviceRepository` para acesso aos dados
