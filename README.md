# 📱 API de Gerenciamento de Dispositivos

![Banner da API de Dispositivos](https://via.placeholder.com/1200x300?text=Devices+API+Management)
<!-- Substitua a URL acima por uma imagem de banner relacionada a gerenciamento de dispositivos ou tecnologia -->

## 📋 Visão Geral

Esta API RESTful permite o gerenciamento completo de dispositivos com operações CRUD e validações de domínio específicas. Desenvolvida com Spring Boot e arquitetura MVC, oferece uma solução robusta para controle de inventário de dispositivos.

![Arquitetura da Aplicação](https://via.placeholder.com/800x400?text=Arquitetura+MVC+Spring+Boot)
<!-- Substitua a URL acima por um diagrama da arquitetura MVC da sua aplicação -->

## ✨ Funcionalidades

| Funcionalidade | Descrição | Endpoint |
|----------------|-----------|----------|
| 🆕 Criar dispositivo | Adiciona um novo dispositivo ao sistema | `POST /api/v1/devices` |
| 🔍 Buscar por ID | Recupera um dispositivo específico | `GET /api/v1/devices/{id}` |
| 📋 Listar todos | Lista todos os dispositivos cadastrados | `GET /api/v1/devices` |
| 🏷️ Filtrar por marca | Lista dispositivos de uma marca específica | `GET /api/v1/devices/brand/{brand}` |
| 🔄 Filtrar por estado | Lista dispositivos em um estado específico | `GET /api/v1/devices/state/{state}` |
| ✏️ Atualização completa | Atualiza todos os campos de um dispositivo | `PUT /api/v1/devices/{id}` |
| 🔄 Atualização parcial | Atualiza apenas os campos fornecidos | `PATCH /api/v1/devices/{id}` |
| 🗑️ Exclusão | Remove um dispositivo do sistema | `DELETE /api/v1/devices/{id}` |

### Estados de Dispositivos

![Estados de Dispositivos](https://via.placeholder.com/600x200?text=Estados+dos+Dispositivos)
<!-- Substitua por um diagrama mostrando os três estados possíveis e suas transições -->

- **🟢 AVAILABLE**: Dispositivo disponível para uso
- **🔵 IN_USE**: Dispositivo atualmente em uso (com restrições para atualização e exclusão)
- **🔴 INACTIVE**: Dispositivo inativo/fora de funcionamento

## 📏 Regras de Negócio

- ⏰ **Tempo de criação não pode ser atualizado**
  - O campo `creationTime` é definido automaticamente na criação e não pode ser modificado

- 🔒 **Proteção para dispositivos em uso**
  - Nome e marca não podem ser atualizados se o dispositivo estiver com estado `IN_USE`
  - Dispositivos com estado `IN_USE` não podem ser excluídos

![Regras de Negócio](https://via.placeholder.com/800x400?text=Regras+de+Negócio+Ilustradas)
<!-- Substitua por um diagrama ilustrando as regras de negócio -->

## 🛠️ Tecnologias Utilizadas

![Stack Tecnológica](https://via.placeholder.com/900x300?text=Stack+Tecnológica)
<!-- Substitua por uma imagem mostrando os logos das tecnologias utilizadas -->

- **Backend**: Java 21, Spring Boot 3.2.2
- **Persistência**: Spring Data JPA, PostgreSQL
- **Documentação**: Swagger/OpenAPI
- **Containerização**: Docker, Docker Compose
- **Testes**: JUnit 5, TestContainers, MockMvc

## 📱 Modelo de Dados

O modelo de domínio `Device` possui os seguintes atributos:

```json
{
  "id": 1,
  "name": "Galaxy S23",
  "brand": "Samsung",
  "state": "AVAILABLE",
  "creationTime": "2025-05-31T12:00:00"
}
```

![Modelo de Dados](https://via.placeholder.com/600x400?text=Diagrama+de+Classe+Device)
<!-- Substitua por um diagrama UML da classe Device -->

## 🚀 Como Executar

### Pré-requisitos

- Docker e Docker Compose
- Java 21+ (apenas para execução local)
- Maven 3.9+ (apenas para execução local)

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

![Docker Compose](https://via.placeholder.com/800x400?text=Docker+Compose+em+Ação)
<!-- Substitua por uma screenshot do terminal executando docker-compose up -->

3. Acesse a API em: [http://localhost:8080/api/v1/devices](http://localhost:8080/api/v1/devices)

### Localmente

1. Configure o PostgreSQL:
```bash
# Inicie apenas o container PostgreSQL
docker-compose up postgres
```

2. Execute o projeto usando Maven:
```bash
./mvnw spring-boot:run
```

## 📘 Documentação da API

A documentação interativa da API está disponível via Swagger UI após iniciar a aplicação:

![Swagger UI](https://via.placeholder.com/900x500?text=Screenshot+do+Swagger+UI)
<!-- Substitua por uma screenshot real da página Swagger da sua API -->

Acesse: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🧪 Testes

O projeto inclui testes unitários e de integração para validar o comportamento da aplicação:

- **Testes de Serviço**: Validam a lógica de negócio e regras de validação
- **Testes de Integração**: Verificam o comportamento dos endpoints da API
- **TestContainers**: Permitem testar a integração com um banco de dados PostgreSQL real

Para executar os testes:

```bash
./mvnw test
```

![Resultado dos Testes](https://via.placeholder.com/800x400?text=Resultado+dos+Testes)
<!-- Substitua por uma screenshot dos resultados de execução dos testes -->

## 🔄 Exemplo de Uso

### Criando um novo dispositivo

**Request:**

```bash
curl -X POST http://localhost:8080/api/v1/devices \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "brand": "Apple",
    "state": "AVAILABLE"
  }'
```

**Response:**

```json
{
  "id": 1,
  "name": "iPhone 15",
  "brand": "Apple",
  "state": "AVAILABLE",
  "creationTime": "2025-05-31T14:30:15.123456"
}
```

### Listando dispositivos por marca

**Request:**

```bash
curl http://localhost:8080/api/v1/devices/brand/Apple
```

**Response:**

```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "brand": "Apple",
    "state": "AVAILABLE",
    "creationTime": "2025-05-31T14:30:15.123456"
  },
  {
    "id": 3,
    "name": "iPad Pro",
    "brand": "Apple",
    "state": "INACTIVE",
    "creationTime": "2025-05-30T10:15:30.987654"
  }
]
```

![Exemplo de Uso com Postman](https://via.placeholder.com/800x500?text=Exemplo+de+Uso+com+Postman)
<!-- Substitua por screenshots de requisições reais usando Postman ou ferramenta similar -->

## 📦 Estrutura do Projeto

O projeto segue o padrão arquitetural MVC (Model-View-Controller):

```
src/
├── main/
│   ├── java/
│   │   └── com/desafio/tecnico/
│   │       ├── config/              # Configurações da aplicação
│   │       ├── controller/          # Controladores REST
│   │       ├── dto/                 # Objetos de transferência de dados
│   │       ├── exception/           # Exceções personalizadas e handler
│   │       ├── model/               # Entidades JPA
│   │       ├── repository/          # Repositórios de dados
│   │       └── service/             # Lógica de negócio
│   └── resources/
│       └── application.properties   # Configurações da aplicação
└── test/
    └── java/
        └── com/desafio/tecnico/
            ├── controller/          # Testes de controladores
            └── service/             # Testes de serviços
```

![Estrutura do Projeto](https://via.placeholder.com/700x500?text=Estrutura+do+Projeto)
<!-- Substitua por uma imagem da estrutura do projeto em uma IDE como IntelliJ ou VS Code -->

## 🔮 Possíveis Melhorias Futuras

- 🔐 **Segurança**: Implementar autenticação e autorização com JWT ou OAuth2
- 📄 **Paginação**: Adicionar suporte à paginação nos endpoints de listagem
- 📊 **Monitoramento**: Integrar Prometheus e Grafana para monitoramento em tempo real
- 🌐 **HATEOAS**: Implementar HATEOAS para melhor navegabilidade da API
- 🔄 **Cache**: Adicionar cache para operações de leitura frequentes
- 📝 **Histórico**: Registrar histórico de mudanças de estado dos dispositivos
- 🏷️ **Categorias**: Adicionar suporte a categorias/tipos de dispositivos

## 📜 Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

## 👨‍💻 Autor

Desenvolvido como parte de um desafio técnico.

![Obrigado](https://via.placeholder.com/500x200?text=Obrigado+por+usar+nossa+API!)
<!-- Substitua por uma imagem personalizada de agradecimento -->
