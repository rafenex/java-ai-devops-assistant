# Java AI DevOps Assistant

Projeto backend desenvolvido em **Java 21** com **Spring Boot**, criado para simular uma aplicação moderna de assistente para análise de erros com integração com IA, banco de dados, mensageria, observabilidade e automação de build.

O objetivo do projeto é praticar e demonstrar conhecimentos de backend, DevOps e arquitetura de aplicações cloud-ready, com foco em boas práticas usadas em ambientes profissionais.

## Tecnologias utilizadas

* Java 21
* Spring Boot 3
* Spring Web
* Spring Data JPA
* Spring Boot Actuator
* Spring AI
* OpenAI API
* PostgreSQL
* H2 Database para testes
* Flyway
* RabbitMQ
* Docker
* Docker Compose
* Maven
* GitHub Actions
* Micrometer
* Prometheus
* Grafana

## Funcionalidades atuais

* API REST para análise de erros com IA
* Integração com provedor de IA via OpenAI
* Persistência de análises no PostgreSQL
* Migrations de banco com Flyway
* Profile de testes usando H2
* Testes automatizados
* Pipeline CI com GitHub Actions
* Containerização com Docker
* Orquestração local com Docker Compose
* Healthcheck da aplicação usando Spring Boot Actuator
* Endpoint `/actuator/health`
* Endpoint `/actuator/info`
* Endpoint `/actuator/prometheus`
* Informações de build geradas pelo Maven
* Processamento assíncrono com RabbitMQ
* Controle de status da análise assíncrona:

    * `PENDING`
    * `PROCESSING`
    * `DONE`
    * `ERROR`
* Observabilidade com Prometheus e Grafana

## Arquitetura inicial

A aplicação segue uma estrutura simples em camadas:

```text
src
 └── main
     └── java
         └── br.com.rafael.aiassistant
             ├── ai
             ├── config
             ├── controller
             ├── dto
             ├── exception
             ├── messaging
             │   ├── consumer
             │   ├── dto
             │   └── producer
             ├── model
             ├── repository
             └── service
```

Responsabilidades principais:

* `controller`: expõe os endpoints REST
* `service`: contém as regras de negócio
* `repository`: acesso ao banco de dados
* `model`: entidades persistidas
* `dto`: objetos de entrada e saída da API
* `ai`: abstração dos provedores de IA
* `messaging`: producer, consumer e DTOs de mensageria
* `config`: configurações da aplicação
* `exception`: tratamento de erros da API

## Fluxo síncrono

Endpoint:

```http
POST /api/error-analyses
```

Fluxo:

```text
Cliente envia erro
        ↓
API chama o provedor de IA
        ↓
Resultado é salvo no banco
        ↓
API retorna a análise completa
```

Esse fluxo aguarda a resposta da IA na mesma requisição.

## Fluxo assíncrono com RabbitMQ

Endpoint:

```http
POST /api/error-analyses/async
```

Fluxo:

```text
Cliente envia erro
        ↓
API salva análise com status PENDING
        ↓
Producer envia o ID da análise para o RabbitMQ
        ↓
API retorna 202 Accepted com ID e status
        ↓
Consumer recebe o ID
        ↓
Consumer marca a análise como PROCESSING
        ↓
Consumer chama o provedor de IA
        ↓
Consumer salva o resultado
        ↓
Consumer marca como DONE
```

Em caso de erro no processamento:

```text
PENDING -> PROCESSING -> ERROR
```

Esse fluxo permite processar análises em background sem bloquear a requisição do cliente.

## Endpoints principais

### Criar análise síncrona

```http
POST /api/error-analyses
```

Exemplo de request:

```json
{
  "title": "Erro ao subir aplicação Spring Boot",
  "context": "Aplicação falha ao iniciar após configurar RabbitMQ",
  "stacktrace": "UnsatisfiedDependencyException: Error creating bean..."
}
```

Exemplo de response:

```json
{
  "id": 1,
  "probableCause": "Falha na criação de bean",
  "whereToLook": "Verifique as dependências da aplicação",
  "suggestedFix": "Corrija a configuração do bean ausente",
  "risk": "Aplicação não inicia",
  "category": "SPRING"
}
```

### Criar análise assíncrona

```http
POST /api/error-analyses/async
```

Exemplo de request:

```json
{
  "title": "Erro ao subir aplicação Spring Boot",
  "context": "Aplicação falha ao iniciar após configurar RabbitMQ",
  "stacktrace": "UnsatisfiedDependencyException: Error creating bean..."
}
```

Exemplo de response:

```json
{
  "id": 10,
  "status": "PENDING"
}
```

### Listar histórico de análises

```http
GET /api/error-analyses
```

Exemplo de response:

```json
[
  {
    "id": 10,
    "title": "Erro ao subir aplicação Spring Boot",
    "category": "SPRING",
    "provider": "openai",
    "status": "DONE",
    "createdAt": "2026-06-25T20:00:00"
  }
]
```

### Buscar detalhe de uma análise

```http
GET /api/error-analyses/{id}
```

Exemplo de response:

```json
{
  "id": 10,
  "title": "Erro ao subir aplicação Spring Boot",
  "context": "Aplicação falha ao iniciar após configurar RabbitMQ",
  "stacktrace": "UnsatisfiedDependencyException: Error creating bean...",
  "probableCause": "Falha na criação de bean",
  "whereToLook": "Verifique a configuração dos beans",
  "suggestedFix": "Corrija as dependências e propriedades necessárias",
  "risk": "Aplicação não inicia",
  "category": "SPRING",
  "provider": "openai",
  "status": "DONE",
  "createdAt": "2026-06-25T20:00:00",
  "processedAt": "2026-06-25T20:00:10",
  "errorMessage": null
}
```

## Endpoints de monitoramento

### Healthcheck

```http
GET /actuator/health
```

Exemplo de resposta:

```json
{
  "status": "UP"
}
```

Esse endpoint também permite verificar componentes como:

* PostgreSQL
* RabbitMQ
* Disco
* SSL
* Ping

### Informações da aplicação

```http
GET /actuator/info
```

Exemplo de resposta:

```json
{
  "build": {
    "artifact": "aiassistant",
    "name": "aiassistant",
    "time": "2026-06-24T18:44:42.433Z",
    "version": "0.0.1-SNAPSHOT",
    "group": "br.com.rafael"
  }
}
```

### Métricas Prometheus

```http
GET /actuator/prometheus
```

Esse endpoint expõe métricas da aplicação no formato esperado pelo Prometheus.

Exemplos de métricas:

```text
application_ready_time_seconds
http_server_requests_seconds_count
jvm_memory_used_bytes
process_cpu_usage
system_cpu_usage
```

## Observabilidade

A aplicação expõe métricas usando **Spring Boot Actuator**, **Micrometer** e **Prometheus**.

O stack local inclui:

* Prometheus
* Grafana

URLs locais:

```text
Prometheus: http://localhost:9090
Grafana:    http://localhost:3000
```

Login padrão do Grafana:

```text
usuário: admin
senha: admin
```

No Grafana, configure o datasource Prometheus usando:

```text
http://prometheus:9090
```

Exemplos de queries para gráficos:

```promql
jvm_memory_used_bytes
```

```promql
http_server_requests_seconds_count
```

```promql
process_cpu_usage
```

```promql
application_ready_time_seconds
```

## Como executar localmente

### Pré-requisitos

Antes de iniciar, é necessário ter instalado:

* Java 21
* Maven
* Docker
* Docker Compose

### Clonar o projeto

```bash
git clone https://github.com/seu-usuario/java-ai-devops-assistant.git
cd java-ai-devops-assistant
```

### Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
OPENAI_API_KEY=sua_chave_da_openai
```

Também é recomendado manter um arquivo `.env.example` versionado:

```env
OPENAI_API_KEY=your_openai_api_key_here
```

O arquivo `.env` real não deve ser commitado.

Garanta que ele esteja no `.gitignore`:

```gitignore
.env
```

## Executando com Docker Compose

Para subir a aplicação junto com PostgreSQL, RabbitMQ, Prometheus e Grafana:

```bash
docker-compose up --build
```

Ou em background:

```bash
docker-compose up --build -d
```

A API ficará disponível em:

```text
http://localhost:8080
```

RabbitMQ Management:

```text
http://localhost:15672
```

Prometheus:

```text
http://localhost:9090
```

Grafana:

```text
http://localhost:3000
```

## Serviços Docker

O ambiente local sobe os seguintes serviços:

* `api`: aplicação Spring Boot
* `postgres`: banco PostgreSQL
* `rabbitmq`: broker de mensageria
* `prometheus`: coleta de métricas
* `grafana`: visualização de métricas

## Docker Healthcheck

O serviço da API possui healthcheck configurado no `docker-compose.yml`, utilizando o endpoint:

```text
/actuator/health
```

Isso permite que o Docker acompanhe se a aplicação está saudável após subir o container.

## Executando os testes

Para executar os testes automatizados:

```bash
mvn test
```

Ou:

```bash
mvn clean test
```

Os testes utilizam o profile `test` com banco H2 em memória.

## Build da aplicação

Para gerar o pacote da aplicação:

```bash
mvn clean package
```

O arquivo `.jar` será gerado em:

```text
target/
```

## CI com GitHub Actions

O projeto possui pipeline de CI com GitHub Actions.

O workflow executa automaticamente em:

* `push`
* `pull_request`

Etapas principais:

* Checkout do código
* Configuração do Java 21
* Cache do Maven
* Execução dos testes automatizados

## Roadmap

* [x] README profissional
* [x] Actuator health/info
* [x] Build info Maven
* [x] Profile de testes com H2
* [x] GitHub Actions CI
* [x] RabbitMQ para processamento assíncrono
* [x] Status `PENDING`, `PROCESSING`, `DONE` e `ERROR`
* [x] Métricas Prometheus via Actuator
* [x] Prometheus
* [x] Grafana
* [ ] Kubernetes manifests
* [ ] Testes de integração
* [ ] Swagger/OpenAPI
* [ ] Melhorias de segurança
* [ ] Versionamento de dashboard Grafana

## Objetivo de aprendizado

Este projeto foi criado com foco em evolução profissional para backend Java, praticando conceitos usados em aplicações reais:

* APIs REST
* Integração com serviços externos
* Testes automatizados
* Containers
* Healthcheck
* Observabilidade
* CI/CD
* Mensageria
* Processamento assíncrono
* Monitoramento com Prometheus
* Visualização com Grafana
* Boas práticas de arquitetura backend
* Preparação para ambientes cloud-ready

## Autor

Desenvolvido por Rafael Fernandes.
