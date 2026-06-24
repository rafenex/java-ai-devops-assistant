# Java AI DevOps Assistant

AI-powered Java/Spring Boot assistant for analyzing technical errors.

## Current features

- Spring Boot API
- Error analysis endpoint
- AI provider abstraction
- Fake AI provider for local testing

## Endpoint

### Analyze technical error

```http
POST /api/error-analyses
```

Example request:

```json
{
  "title": "Erro ao subir aplicação",
  "context": "Spring Boot 3 com Oracle",
  "stacktrace": "org.springframework.beans.factory.UnsatisfiedDependencyException..."
}
```

## Roadmap

- Add OpenAI provider with Spring AI
- Persist analyses with PostgreSQL
- Add Docker
- Add CI/CD
- Add Kubernetes
