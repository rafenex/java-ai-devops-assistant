# Java AI DevOps Assistant

Projeto backend desenvolvido em **Java 21** com **Spring Boot**, criado para simular uma aplicação moderna de assistente com integração com IA, banco de dados, Docker, healthcheck e observabilidade básica.

O objetivo do projeto é praticar e demonstrar conhecimentos de backend, DevOps e arquitetura de aplicações cloud-ready, com foco em boas práticas usadas em ambientes profissionais.

## Tecnologias utilizadas

* Java 21
* Spring Boot 3
* Spring Web
* Spring Data JPA
* PostgreSQL
* H2 Database para testes
* Docker
* Docker Compose
* Spring Boot Actuator
* Maven
* OpenAI API

## Funcionalidades atuais

* API REST com Spring Boot
* Integração com provedor de IA via OpenAI
* Persistência com PostgreSQL
* Profile de testes usando H2
* Containerização com Docker
* Orquestração local com Docker Compose
* Healthcheck da aplicação usando Actuator
* Endpoint `/actuator/health`
* Endpoint `/actuator/info`
* Informações de build geradas pelo Maven
* Testes automatizados com profile `test`

## Arquitetura inicial

A aplicação segue uma estrutura simples em camadas:

```text
src
 └── main
     └── java
         └── br.com.rafael.aiassistant
             ├── controller
             ├── service
             ├── repository
             ├── config
             ├── dto
             └── model
```

Responsabilidades principais:

* `controller`: expõe os endpoints REST
* `service`: contém as regras de negócio
* `repository`: acesso ao banco de dados
* `model`: entidades persistidas
* `dto`: objetos de entrada e saída da API
* `config`: configurações da aplicação

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

## Executando com Docker Compose

Para subir a aplicação junto com o PostgreSQL:

```bash
docker-compose up --build
```

A API ficará disponível em:

```text
http://localhost:8080
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

## Executando os testes

Para executar os testes automatizados:

```bash
mvn test
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

## Docker Healthcheck

O serviço da API possui healthcheck configurado no `docker-compose.yml`, utilizando o endpoint:

```text
/actuator/health
```

Isso permite que o Docker acompanhe se a aplicação está saudável após subir o container.

## Roadmap

Próximas melhorias planejadas:

* Configurar pipeline CI com GitHub Actions
* Adicionar RabbitMQ para processamento assíncrono
* Expor métricas do Actuator
* Integrar Prometheus e Grafana
* Criar manifests Kubernetes
* Adicionar testes de integração
* Melhorar tratamento de erros
* Documentar endpoints com Swagger/OpenAPI

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
* Kubernetes
* Boas práticas de arquitetura backend

## Autor

Desenvolvido por Rafael Fernandes.
