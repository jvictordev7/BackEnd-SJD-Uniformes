# BackEnd-SJD-Uniformes

API em Spring Boot para gestão de clientes, produtos, pedidos e caixa do sistema SJD Uniformes. Inclui cadastro, fluxo de produção/pagamento de pedidos, registro de lançamentos no caixa e autenticação JWT (rotas abertas por enquanto para facilitar testes).

## Tecnologias
- Java 17, Spring Boot 4.x (Web, Data JPA, Validation, Security), Lombok
- JWT (jjwt 0.11.5)
- Banco: MySQL/TiDB
- Maven

## Principais recursos
- Clientes: CRUD com datas automáticas.
- Categorias e Produtos: cadastro, ativo/inativo e associação de categoria.
- Pedidos: número `PED-xxxx`, itens com totalização, etapas de produção, status de pagamento.
- Pagamentos: registro e atualização de status (NAO_PAGO, PAGO_50, PAGO_TOTAL) + lançamento automático no caixa.
- Caixa: lançamentos de entrada/saída com origem rastreável (`PEDIDO`).
- Autenticação: endpoints `/auth/register` e `/auth/login` geram JWT (filtro presente; segurança ainda `permitAll` para desenvolvimento).

## Endpoints principais
- Auth: `POST /auth/register`, `POST /auth/login` (retorna token `Bearer ...`)
- Clientes: `GET/POST /api/clientes`, `GET /api/clientes/{id}`, `DELETE /api/clientes/{id}`
- Produtos: `GET/POST /api/produtos`
- Pedidos: `GET/POST /api/pedidos`, `GET /api/pedidos/{id}`, `PUT /api/pedidos/{id}/etapa`, `PUT /api/pedidos/{id}/pagamento`

## Configuração
- Banco: ajustar URL/usuario/senha em `src/main/resources/application.properties` (`spring.datasource.*`).
- JWT: definir `jwt.secret` (string longa/aleatória) e `jwt.expiration` (ms).
- Credenciais Cloudinary: preencher se for usar upload de mídia.

## Como rodar
```bash
./mvnw spring-boot:run
```
Usando Maven instalado:
```bash
mvn clean spring-boot:run
```
Porta padrão: `http://localhost:8080`.

## Teste rápido
1) Registrar usuário:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Admin","email":"admin@example.com","senha":"123456","role":"ADMIN"}'
```
2) Login e token:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"123456"}'
```
3) Usar token nas chamadas:
```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/clientes
```

