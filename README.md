# AI Sandbox Agent — Full-stack starter

Includes Spring Boot REST API, PostgreSQL/JPA, HTTP Basic auth, configurable OpenAI-compatible planning (deterministic demo fallback), fixed-command Docker smoke test, React dashboard, Docker Compose and a backend context test.

## Safety / honest scope
This is a local-development learning starter, not a production autonomous agent or hostile-code execution service. The sandbox only runs a fixed Alpine smoke-test command; it never executes user-supplied shell/code. Docker daemon access is privileged. Do not expose this service publicly without a separately hardened worker, authorization, quotas, audit logs, secret isolation, image pinning and network controls.

Without `AI_API_KEY`, the planner runs deterministic demo mode (not an LLM). With a compatible provider key, it calls `/v1/chat/completions`.

## Run
1. Install JDK 21+, Maven 3.9+, Docker Desktop, Node 20+.
2. Copy `.env.example` to `.env` and change passwords.
3. `docker compose up --build`
4. Frontend: http://localhost:5173 ; API: http://localhost:8082
5. Default local credentials: `admin` / `change-this-password` (change before use).

## Local backend
`cd backend` then `mvn spring-boot:run`. It uses an in-memory H2 database by default.
## Local frontend
`cd frontend` then `npm install` and `npm run dev`.

## API (HTTP Basic)
- `GET /api/health` public
- `GET /api/tasks`
- `POST /api/tasks` body `{"title":"Demo task","description":"..." }`
- `GET /api/tasks/{id}`
- `PATCH /api/tasks/{id}/status` body `{"status":"COMPLETED"}`
- `DELETE /api/tasks/{id}`
- `POST /api/agent/run` body `{"instruction":"Plan a safe demo"}`
- `GET /api/agent/runs`, `GET /api/agent/runs/{id}`
- `POST /api/sandbox/smoke-test` fixed Docker test

## AI settings
`AI_BASE_URL` defaults to `https://api.openai.com`; `AI_API_KEY` blank means demo mode; `AI_MODEL` defaults to `gpt-4o-mini`. Never commit `.env`.
