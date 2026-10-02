# Contrato da API - Frontend Hospital Management (Torres)

Este documento foi gerado a partir do backend atual para permitir que o frontend trabalhe de forma independente, com endpoints, payloads, respostas, erros e permissões já alinhados ao código.

## 1) Base da API

- Base principal: `http://localhost:8080/api/v1`
- Alias também suportado: `http://localhost:8080/v1`
- Autenticação: JWT via header `Authorization: Bearer <token>`
- CORS: habilitado para qualquer origem com `*` nas origens e métodos `GET`, `POST`, `PUT`, `DELETE`, `PATCH`, `OPTIONS`
- Segurança: todas as rotas, exceto `/auth/**`, exigem token válido e autorização por role

## 2) Modelo de resposta de erro

Todos os erros seguem o formato abaixo:

```json
{
  "timestamp": "2025-01-01T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem descritiva do erro",
  "path": "/api/v1/paciente"
}
```

### Status HTTP comuns

- 200 OK: operação concluída com sucesso
- 201 Created: criação realizada
- 204 No Content: deleção/ação concluída sem corpo
- 400 Bad Request: regra de negócio / validação / payload inválido
- 401 Unauthorized: credenciais inválidas ou token ausente/expirado
- 403 Forbidden: token válido, mas sem permissão suficiente
- 404 Not Found: recurso inexistente
- 409 Conflict: conflito de dados (email duplicado, código duplicado, etc.)
- 500 Internal Server Error: erro inesperado do servidor

## 3) Autenticação

### 3.1 POST /api/v1/auth/login

- Permissão: pública
- Body:

```json
{
  "email": "admin@torres.com",
  "password": "123456"
}
```

- Sucesso (200):

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "mensagem": "Autenticação realizada com sucesso",
  "usuario": {
    "id": "uuid",
    "nome": "Nome do Usuário",
    "email": "admin@torres.com",
    "cargo": "ADMIN",
    "codigoFuncionario": "ADM-001",
    "status": "ATIVO"
  }
}
```

- Erros:
  - 400: `Email e senha são obrigatórios.`
  - 401: `Email ou senha incorretos, ou usuário sem permissão ativa.`

### 3.2 GET /api/v1/auth/me

- Permissão: qualquer usuário autenticado
- Headers: `Authorization: Bearer <token>`
- Sucesso (200): retorna o usuário autenticado no mesmo formato do campo `usuario` do login
- Erros:
  - 401: sem token ou usuário inválido
  - 404: usuário não encontrado

### 3.3 POST /api/v1/auth/register

- Permissão: pública
- Body:

```json
{
  "name": "João Silva",
  "email": "joao@hospital.com",
  "password": "123456",
  "cargo": "MEDICO",
  "codigoFuncionario": "MED-001"
}
```

- Sucesso (201):

```json
"Solicitação de cadastro enviada com sucesso! O acesso será validado pela coordenação."
```

- Erros:
  - 400: campos obrigatórios ausentes / senha com menos de 6 caracteres
  - 409: email ou código de funcionário duplicado

## 4) Roles e permissões principais

Enum de cargos:

- `ADMIN`
- `ENFERMEIRO`
- `MEDICO`
- `FARMACEUTICO`
- `RECEPCIONISTA`
- `TECNICO`
- `LABORATORISTA`

Regra geral:

- `ADMIN`: acesso total
- `MEDICO` / `ENFERMEIRO` / `RECEPCIONISTA` / `FARMACEUTICO` / `TECNICO` / `LABORATORISTA`: acesso por módulo conforme regras definidas em cada endpoint

## 5) Gestão de Pacientes

### 5.1 POST /api/v1/paciente

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Body (modelo `Paciente`):

```json
{
  "nome": "Maria Souza",
  "bi": "123456789L",
  "genero": "FEMININO",
  "idade": "32",
  "dataNascimento": "1993-02-15",
  "status": "ATIVO",
  "seguro": "Particular",
  "telefone": "912345678",
  "tipoSanguineo": "A+",
  "alergias": "Penicilina",
  "doencasPreExistentes": "Hipertensão",
  "ultimaPressaoAferida": "120/80"
}
```

- Sucesso (200/201): retorna o paciente salvo
- Erros: 400 / 404 / 409 conforme validações do serviço

### 5.2 GET /api/v1/paciente

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Query params opcionais:
  - `nome`
  - `status`
- Sucesso (200): lista de pacientes

### 5.3 GET /api/v1/paciente/{id}

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Sucesso: objeto `Paciente`
- Erro: 404 se paciente não existir

### 5.4 PUT /api/v1/paciente/{id}

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Body: mesmo formato do cadastro, com atualização parcial ou completa
- Sucesso: paciente atualizado

### 5.5 DELETE /api/v1/paciente/{id}

- Permissões: `ADMIN`, `MEDICO`, `RECEPCIONISTA`
- Sucesso: 204 sem corpo

## 6) Médicos

### 6.1 GET /api/v1/medicos
### 6.2 GET /api/v1/medicos/ativos
### 6.3 GET /api/v1/medicos/{id}
### 6.4 POST /api/v1/medicos
### 6.5 PUT /api/v1/medicos/{id}
### 6.6 PATCH /api/v1/medicos/{id}/status?ativo=true|false

- Permissões gerais: listagem e consulta: `ADMIN`, `MEDICO`, `RECEPCIONISTA`, `ENFERMEIRO`
- Criação/atualização/status: `ADMIN`, `RECEPCIONISTA`
- Body de criação/edição: objeto `Medico`
- Sucesso: objeto `Medico`
- Erros: 400/404/409 conforme regra do serviço

## 7) Enfermeiros

### 7.1 GET /api/v1/enfermeiros
### 7.2 GET /api/v1/enfermeiros/ativos
### 7.3 GET /api/v1/enfermeiros/{id}
### 7.4 POST /api/v1/enfermeiros
### 7.5 PUT /api/v1/enfermeiros/{id}
### 7.6 PATCH /api/v1/enfermeiros/{id}/status?ativo=true|false

- Permissões gerais: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Criação/atualização/status: `ADMIN`, `RECEPCIONISTA`, `MEDICO`, `ENFERMEIRO`
- Body: objeto `Enfermeiro`

## 8) Funcionários e Recursos Humanos

### 8.1 GET /api/v1/funcionarios
### 8.2 GET /api/v1/funcionarios/{id}
### 8.3 GET /api/v1/funcionarios/codigo/{codigoFuncionario}
### 8.4 POST /api/v1/funcionarios
### 8.5 PUT /api/v1/funcionarios/{id}
### 8.6 PATCH /api/v1/funcionarios/{id}/status?ativo=true|false

- Permissões de leitura: `ADMIN`, `RECEPCIONISTA`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`, `TECNICO`
- Criação/atualização/status: `ADMIN`, `RECEPCIONISTA`

## 9) Unidades Hospitalares

### 9.1 GET /api/v1/unidades
### 9.2 GET /api/v1/unidades/ativas
### 9.3 GET /api/v1/unidades/{id}
### 9.4 POST /api/v1/unidades
### 9.5 PUT /api/v1/unidades/{id}
### 9.6 PATCH /api/v1/unidades/{id}/status?ativa=true|false

- Leitura: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`, `RECEPCIONISTA`, `TECNICO`, `LABORATORISTA`
- Criação/edição/status: apenas `ADMIN`

## 10) Áreas e Leitos

### 10.1 GET /api/v1/alas
### 10.2 GET /api/v1/alas/{id}
### 10.3 POST /api/v1/alas
### 10.4 PUT /api/v1/alas/{id}

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA` para leitura; edição: `ADMIN`, `MEDICO`, `ENFERMEIRO`

### 10.5 GET /api/v1/leitos
### 10.6 GET /api/v1/leitos/{id}
### 10.7 POST /api/v1/leitos
### 10.8 PUT /api/v1/leitos/{id}
### 10.9 PATCH /api/v1/leitos/{id}/status?status=<STATUS>

- Permissões: leitura `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`; criação/edição `ADMIN`, `MEDICO`, `ENFERMEIRO`
- Exemplo de status de leito: conforme enum do modelo `Leito` e regras de uso

## 11) Agendamento

### 11.1 GET /api/v1/agendamentos
### 11.2 GET /api/v1/agendamentos/{id}
### 11.3 POST /api/v1/agendamentos
### 11.4 PATCH /api/v1/agendamentos/{id}/status?status=<STATUS>
### 11.5 DELETE /api/v1/agendamentos/{id}

- Permissões: leitura e agendamento: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Exclusão: `ADMIN`, `MEDICO`, `RECEPCIONISTA`
- Body: objeto `Agendamento`
- Sucesso: objeto agendamento ou 204 para exclusão

## 12) Consulta, Triagem e Internamento (fluxo clínico)

### 12.1 GET /api/v1/consulta
### 12.2 GET /api/v1/consulta/{id}
### 12.3 POST /api/v1/consulta
### 12.4 PATCH /api/v1/consulta/{id}/status?status=<STATUS>
### 12.5 DELETE /api/v1/consulta/{id}

- Permissões: leitura `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Criação e atualização: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA`
- Cancelamento: `ADMIN`, `MEDICO`, `RECEPCIONISTA`

### 12.2 POST /api/v1/triagem
### 12.3 GET /api/v1/triagem/fila
### 12.4 PATCH /api/v1/triagem/{id}/status?status=<STATUS>
### 12.5 DELETE /api/v1/triagem/{id}

- Permissões: `ADMIN`, `ENFERMEIRO`, `MEDICO`, `RECEPCIONISTA`
- Body: objeto `Triagem`
- Sucesso: triagem criada ou atualizada

### 12.6 POST /api/v1/internamentos
### 12.7 POST /api/v1/internamentos/{id}/evolucao
### 12.8 POST /api/v1/internamentos/{id}/alta
### 12.9 PATCH /api/v1/internamentos/{id}/status?status=<STATUS>
### 12.10 POST /api/v1/internamentos/{id}/transferir?novoLeitoId=<id>

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`
- Body para admissão: `InternamentoRequest`
- Body para evolução: `EvolucaoRequest`
- Sucesso: `Internamento` ou `EvolucaoClinica`

### 12.11 GET /api/v1/internamentos/{internamentoId}/evolucoes
### 12.12 POST /api/v1/internamentos/{internamentoId}/evolucoes

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`
- Body: `EvolucaoRequest`

## 13) Medicamentos e Estoque

### 13.1 GET /api/v1/medicamentos
### 13.2 GET /api/v1/medicamentos/{id}
### 13.3 POST /api/v1/medicamentos
### 13.4 PUT /api/v1/medicamentos/{id}
### 13.5 PATCH /api/v1/medicamentos/{id}/estoque?quantidade=<valor>
### 13.6 DELETE /api/v1/medicamentos/{id}

- Permissões de leitura: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`, `RECEPCIONISTA`
- Criação e ajuste de estoque: `ADMIN`, `FARMACEUTICO`, `ENFERMEIRO`
- Edição: `ADMIN`, `FARMACEUTICO`
- Exclusão: `ADMIN`, `FARMACEUTICO`

### 13.7 GET /api/v1/estoque
### 13.8 GET /api/v1/estoque/alerta
### 13.9 POST /api/v1/estoque/movimentacao

- Permissões: leitura `ADMIN`, `FARMACEUTICO`, `ENFERMEIRO`, `MEDICO`
- Registro de movimentação: `ADMIN`, `FARMACEUTICO`
- Body de movimentação: `EstoqueMovimentoRequest`

## 14) Farmácia

### 14.1 GET /api/v1/farmacia/prescricoes
### 14.2 POST /api/v1/farmacia/dispensar

- Permissões: prescrições: `ADMIN`, `FARMACEUTICO`, `MEDICO`, `ENFERMEIRO`
- Dispensação: apenas `ADMIN`, `FARMACEUTICO`
- Body: `DispensacaoRequest`
- Sucesso: `Dispensacao`

## 15) Financeiro

### 15.1 GET /api/v1/financeiro/faturas
### 15.2 GET /api/v1/financeiro/faturas/{id}
### 15.3 POST /api/v1/financeiro/faturas
### 15.4 GET /api/v1/financeiro/pagamentos
### 15.5 POST /api/v1/financeiro/faturas/{id}/pagamentos

- Permissões: listagem de faturas: `ADMIN`, `RECEPCIONISTA`, `MEDICO`
- Criação de faturas e pagamentos: `ADMIN`, `RECEPCIONISTA`
- Body de fatura: `FaturaRequest`
- Body de pagamento: `PagamentoRequest`
- Sucesso: `Fatura` ou `Pagamento`

## 16) Dashboard e KPIs

### 16.1 GET /api/v1/dashboard
### 16.2 GET /api/v1/dashboard/resumo
### 16.3 GET /api/v1/dashboard/kpis

- Permissões: `ADMIN`, `RECEPCIONISTA`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`
- Sucesso (200): `DashboardResumo`

Modelo de resposta esperada:

```json
{
  "totalPacientes": 125,
  "totalConsultas": 340,
  "totalTriagens": 210,
  "admissoesAtivas": 18,
  "leitosOcupados": 22,
  "receitasPendentes": 14,
  "faturamentoPeriodo": 62850.50,
  "medicamentosEstoqueBaixo": 9
}
```

## 17) Configuração Hospitalar

### 17.1 GET /api/v1/configuracao
### 17.2 PUT /api/v1/configuracao

- Leitura: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`, `RECEPCIONISTA`, `TECNICO`, `LABORATORISTA`
- Edição: apenas `ADMIN`
- Body: objeto `ConfiguracaoHospital`

## 18) Laboratório

### 18.1 GET /api/v1/exames
### 18.2 POST /api/v1/exames
### 18.3 POST /api/v1/exames/{protocolo}/laudo

- Permissões: leitura: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `LABORATORISTA`, `TECNICO`
- Solicitação de exame: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `LABORATORISTA`, `TECNICO`
- Laudo: `ADMIN`, `LABORATORISTA`, `TECNICO`
- Body: `SolicitacaoExame` e `LaudoRequest`

## 19) Notificações

### 19.1 GET /api/v1/notificacoes/usuario/{usuarioId}
### 19.2 GET /api/v1/notificacoes/usuario/{usuarioId}/nao-lidas
### 19.3 POST /api/v1/notificacoes
### 19.4 PATCH /api/v1/notificacoes/{id}/ler

- Permissões: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`, `RECEPCIONISTA`, `TECNICO`
- Body: objeto `Notificacao`

## 20) Auditoria

### 20.1 GET /api/v1/auditoria
### 20.2 GET /api/v1/auditoria/usuario/{usuarioId}
### 20.3 POST /api/v1/auditoria

- Permissões: apenas `ADMIN`
- Body: `LogAuditoria`

## 21) Regras práticas para o frontend

1. Sempre usar o prefixo `/api/v1` no frontend.
2. Todas as requisições autenticadas devem enviar `Authorization: Bearer <token>`.
3. O token vem do endpoint `/auth/login`.
4. Em caso de erro, o frontend deve tratar sempre o padrão abaixo:

```json
{
  "timestamp": "2025-01-01T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Texto do erro",
  "path": "/api/v1/algum-endpoint"
}
```

5. A aplicação também aceita rotas com `/v1` como alias, mas a recomendação é usar `/api/v1` para padronização.
6. A maioria dos endpoints de leitura e escrita retorna o objeto criado/atualizado, enquanto `DELETE` normalmente responde `204 No Content`.

## 22) Resumo executivo para alinhamento de sprint

O backend já entrega os fluxos principais do MVP:

- autenticação + JWT
- cadastro e consulta de pacientes
- médicos, enfermeiros e funcionários
- agenda/consultas/triagem/internamento
- estoque, dispensação e medicamentos
- faturamento e pagamentos
- dashboard e KPIs
- configuração centralizada
- auditoria e notificações

A estrutura está pronta para o frontend consumir de forma autônoma, desde que o mesmo trate corretamente:

- JWT
- permissões por role
- payloads JSON
- resposta padrão de erro da API

## 23) Observações finais

- O código atual tem aliases para as rotas (`/api/v1` e `/v1`), e o frontend pode usar qualquer um deles.
- Para evitar ambiguidades de contrato, usar `/api/v1` como padrão.
- Os nomes e estruturas reais dos objetos podem ser confirmados diretamente pelos modelos JPA em `src/main/java/com/us/Torres/models`.

Este documento é a base de integração do frontend com o backend atual.
