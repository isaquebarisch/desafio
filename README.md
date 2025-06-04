# 📱 API de Gerenciamento de Dispositivos

```
 _____            _                   _    _____ _____
|  __ \          (_)                 | |  / ____|  __ \
| |  | | _____   ___  ___ ___  ___   | | | |  __| |__) |
| |  | |/ _ \ \ / / |/ __/ _ \/ __|  | | | | |_ |  ___/
| |__| |  __/\ V /| | (_|  __/\__ \  |_| | |__| | |
|_____/ \___| \_/ |_|\___\___||___/  (_)  \_____|_|
                                                   
          API de Gerenciamento de Dispositivos
```

## 📋 Visão Geral

Esta API RESTful permite o gerenciamento completo de dispositivos com operações CRUD e validações de domínio específicas. Desenvolvida com Spring Boot e arquitetura MVC, oferece uma solução robusta para controle de inventário de dispositivos.

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│     Cliente     │────▶│   Controller    │────▶│     Service     │
│                 │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └────────┬────────┘
                                                         │
                        ┌─────────────────┐     ┌────────▼────────┐
                        │                 │     │                 │
                        │     Modelo      │◀────│   Repository    │
                        │                 │     │                 │
                        └─────────────────┘     └─────────────────┘
                                Arquitetura MVC
```

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

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│    AVAILABLE    │──────▶      IN_USE     │──────▶    INACTIVE     │
│      (🟢)      │      │      (🔵)      │      │      (🔴)      │
└────────┬────────┘      └─────────────────┘      └────────┬────────┘
         │                                                 │
         └─────────────────────────────────────────────────┘
```

- **🟢 AVAILABLE**: Dispositivo disponível para uso
- **🔵 IN_USE**: Dispositivo atualmente em uso (com restrições para atualização e exclusão)
- **🔴 INACTIVE**: Dispositivo inativo/fora de funcionamento

## 📏 Regras de Negócio

- ⏰ **Tempo de criação não pode ser atualizado**
  - O campo `creationTime` é definido automaticamente na criação e não pode ser modificado

- 🔒 **Proteção para dispositivos em uso**
  - Nome e marca não podem ser atualizados se o dispositivo estiver com estado `IN_USE`
  - Dispositivos com estado `IN_USE` não podem ser excluídos

```
┌────────────────────────────────────────────────────────────┐
│                                                            │
│  Regra 1: Não pode atualizar creationTime                  │
│  ─────────────────────────────────────────                 │
│  { "creationTime": "2025-05-31" } ──> ❌ (Não permitido)   │
│                                                            │
│  Regra 2: Não pode atualizar nome/marca quando IN_USE      │
│  ────────────────────────────────────────────────────      │
│  Device(state=IN_USE):                                     │
│    { "name": "Novo nome" } ──> ❌ (Não permitido)          │
│    { "state": "INACTIVE" } ──> ✅ (Permitido)             │
│                                                            │
│  Regra 3: Não pode excluir quando IN_USE                   │
│  ─────────────────────────────────────                     │
│  DELETE /api/v1/devices/123 ──> ❌ (Não permitido)         │
│  se device.state == IN_USE                                 │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

## 🛠️ Tecnologias Utilizadas

```
┌─────────────────┬─────────────────┬─────────────────┐
│    Backend      │   Persistência  │ Documentação    │
├─────────────────┼─────────────────┼─────────────────┤
│  Java 21        │  Spring Data    │  Swagger        │
│  Spring Boot    │  JPA            │  OpenAPI        │
│                 │  PostgreSQL     │                 │
└─────────────────┴─────────────────┴─────────────────┘
┌─────────────────┬─────────────────┐
│ Containerização │     Testes      │
├─────────────────┼─────────────────┤
│  Docker         │  JUnit 5        │
│  Docker Compose │  TestContainers │
│                 │  MockMvc        │
└─────────────────┴─────────────────┘
```

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

```
┌───────────────────────────┐
│         Device            │
├───────────────────────────┤
│ - id: Long                │
│ - name: String            │
│ - brand: String           │
│ - state: DeviceState      │
│ - creationTime: DateTime  │
├───────────────────────────┤
│ + getId(): Long           │
│ + getName(): String       │
│ + getBrand(): String      │
│ + getState(): DeviceState │
│ + getCreationTime(): Date │
│ + setName(String): void   │
│ + setBrand(String): void  │
│ + setState(State): void   │
└───────────────────────────┘
```

## 🚀 Como executar o projeto

### Pré-requisitos
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Docker Compose](https://docs.docker.com/compose/install/) (geralmente já vem com Docker Desktop)

### Clonando o repositório
```bash
git clone [URL_DO_REPOSITÓRIO]
cd Desafio
```

### Executando com Docker
O projeto está configurado para ser executado facilmente com Docker Compose:

```bash
docker-compose up -d
```

Isso irá:
1. Construir a imagem da aplicação
2. Iniciar o banco de dados PostgreSQL
3. Iniciar a API na porta 8080

### Executando sem Docker
Se preferir executar sem Docker:

1. Certifique-se de ter o PostgreSQL instalado e configurado:
   - Database: devices_db
   - Usuário: postgres
   - Senha: postgres
   - Porta: 5432

2. Execute o projeto com Maven:
```bash
./mvnw spring-boot:run
```

## 📖 Documentação da API
A documentação Swagger está disponível em:
```
http://localhost:8080/swagger-ui.html
```

## 🧪 Testes
Para executar os testes:
```bash
./mvnw test
```

[LinkedIn](https://www.linkedin.com/in/isaquebarisch/) | [GitHub](isaquebarisch) | [Portfólio](https://isaquebarisch.github.io/portfolio/) | isaquebarisch@gmail.com