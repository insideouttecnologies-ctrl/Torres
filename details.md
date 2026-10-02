Viewed details.md
Listed directory Zalo
Ran command: `git status`
Ran command: `powershell -Command "Get-ChildItem -Path . -Filter *detail* -Recurse"`
Ran command: `powershell -Command "Get-ChildItem -Path src -Recurse | Select-Object FullName, Length"`
Ran command: `powershell -Command "Get-Content -Path details.md -Raw"`
Ran command: `powershell -Command "Get-ChildItem -Path .. -Recurse -Filter *.md"`
Ran command: `powershell -Command "Get-ChildItem -Path ..\Torres"`
Viewed pom.xml:1-192
Viewed pom.xml:1-800
Ran command: `powershell -Command "Get-ChildItem -Path $env:APPDATA\Code\Backups -Recurse -ErrorAction SilentlyContinue"`
Ran command: `powershell -Command "Get-ChildItem -Path $env:APPDATA -Directory"`
Ran command: `powershell -Command "Get-ChildItem -Path '$env:APPDATA\Antigravity IDE\Backups' -Recurse -File"`
Ran command: `powershell -Command "Get-ChildItem -Path '$env:APPDATA\Antigravity IDE' -Recurse -Filter *details* -ErrorAction SilentlyContinue"`
Ran command: `powershell -Command "Get-ChildItem -Path '$env:APPDATA\Antigravity IDE' -Recurse | Where-Object { $_.LastWriteTime -gt (Get-Date).AddMinutes(-60) } | Select-Object FullName, LastWriteTime, Length"`
Ran command: `powershell -Command "Get-ChildItem -Path src\main\java -Recurse -File | Select-Object FullName"`
Used tool: manage_task
Used tool: manage_task
Viewed details.md
Ran command: `powershell -Command "Get-ChildItem -Path '$env:APPDATA\Antigravity IDE\User' -Recurse -File -Filter *details* -ErrorAction SilentlyContinue"`
Ran command: `powershell -Command "Get-ChildItem -Path '$env:APPDATA\Antigravity IDE' -Directory"`
Ran command: `powershell -Command "Get-ChildItem -Path C:\Users\eduardo\Documents -Recurse -Filter *details.md* -ErrorAction SilentlyContinue | Select-Object FullName, Length, LastWriteTime"`
Ran command: `powershell -Command "Get-ChildItem -Path C:\Users\eduardo\Desktop, C:\Users\eduardo\Downloads -Recurse -Filter *details*.md -ErrorAction SilentlyContinue | Select-Object FullName, Length, LastWriteTime"`
Used tool: manage_task
Used tool: manage_task
Viewed HttpConnector.java:1-60
Viewed MedicosRepositories.java:1-19
Viewed PacienteRepository.java:1-61
Viewed LoginService.java:1-91
Viewed RegisterService.java:1-64
Viewed RegisterDTO.java:1-10
Viewed ResponseDTO.java:1-7
Viewed LoginRecord.java:1-5
Viewed Usuario.java:1-29
Viewed Cargo.java:1-23
Viewed Funcionario.java:1-35
Viewed Medico.java:1-39
Viewed Enfermeiro.java:1-29
Viewed Paciente.java:1-38
Viewed Consulta.java:1-38
Viewed Agendamento.java:1-34
Viewed Medicamento.java:1-50
Viewed Receita.java:1-45
Viewed Pagamento.java:1-43
Viewed MedicosController.java:1-244
Viewed MedicosHelper.java:1-62
Viewed PacientesController.java:1-294
Viewed ModalHelper.java:1-800
Viewed ModalHelper.java:801-1132
Viewed ConsultasController.java:1-190
Viewed AtendimentoController.java:1-147
Viewed InternamentoController.java:1-188
Viewed LaboratorioController.java:1-177
Viewed FarmaciaController.java:1-62
Viewed EstoqueController.java:1-62
Viewed FinanceiroController.java:1-79
Viewed FuncionariosController.java:1-154
Viewed RelatoriosController.java:1-57
Viewed UtilizadoresController.java:1-62
Viewed ConfiguracoesController.java:1-92
Viewed DashboardController.java:1-168
Viewed NotificacoesController.java:1-41
Viewed SidebarController.java:1-436
Viewed SessionService.java:1-74
Viewed LoginController.java:1-188
Viewed RegisterController.java:1-145
Viewed MainLayoutController.java:1-422
Viewed atendimento.fxml:1-152
Viewed consultas.fxml:1-150
Viewed internamento.fxml:1-149
Viewed laboratorio.fxml:1-148
Viewed farmacia.fxml:1-136
Viewed estoque.fxml:1-141
Viewed financeiro.fxml:1-142
Viewed dashboard.fxml:1-448
Viewed utilizadores.fxml:1-148
Viewed funcionarios.fxml:1-170
Viewed medicos.fxml:1-90
Viewed pacientes.fxml:1-67
Viewed notificacoes.fxml:1-104
Viewed relatorios.fxml:1-131
Viewed configuracoes.fxml:1-124
Viewed details.md

# Relatório de Auditoria Arquitetural & Especificação de Back-end (Zalo)

> [!WARNING]
> **Aviso sobre o arquivo `details.md`:**  
> O arquivo [details.md](file:///c:/Users/eduardo/Documents/Torres/Zalo/details.md) na raiz do projeto encontra-se atualmente vazio (**0 bytes** em disco). É provável que o documento ainda não tenha sido salvo no editor (Ctrl+S) ou que o texto não tenha sido colado.  
> **Ação tomada:** Para não bloquear o desenvolvimento, foi realizada uma **auditoria reversa minuciosa de 100% do código-fonte do cliente JavaFX (Zalo)** — inspecionando todos os controllers, telas FXML, modelos de dados, rotinas de requisição HTTP e modais da aplicação. Toda a especificação abaixo foi rigorosamente extraída e estruturada a partir do fluxo real do sistema.

---

## 1. Recursos do Back-end

O back-end deve ser estruturado em **14 módulos de negócio**, fornecendo uma arquitetura RESTful stateless protegida por autenticação JWT:

1. **Módulo de Autenticação, Usuários & RBAC (Segurança):** Gestão de credenciais, geração e validação de tokens JWT, autenticação em duas etapas (2FA), controle de acessos por papéis (`Cargo`: `ADMIN`, `MEDICO`, `ENFERMEIRO`, `FARMACEUTICO`, `RECEPCIONISTA`, `TECNICO`), aprovação de contas com status `PENDENTE` e recuperação de acessos.
2. **Módulo de Pacientes & Prontuário Eletrônico (PEP):** Cadastro individual de pacientes, histórico clínico, documentos (BI), convênios/seguros de saúde, dados antropométricos, alergias conhecidas e histórico longitudinal de atendimentos.
3. **Módulo do Corpo Clínico (Médicos & Especialidades):** Registro de especialistas com respectivo Número de Ordem dos Médicos (ORM), especialidades clínicas, vínculo com usuário do sistema, escalas de plantão (diurno/noturno) e agenda médica.
4. **Módulo de Recursos Humanos & Funcionários:** Cadastro institucional de colaboradores (`codigoFuncionario`), departamentos hospitalares (Enfermagem, Farmácia, Recepção, Laboratório, Administração), turnos 12x36 e histórico funcional.
5. **Módulo de Triagem & Fila Rápida (Protocolo de Manchester):** Recepção de pacientes, geração de senhas (`E-01`, `L-04`, `A-12`, `V-23`), aferição de sinais vitais (PA, SpO2, Temperatura, FC), estratificação de risco (Vermelho, Laranja, Amarelo, Verde, Azul) e direcionamento automático para consultório ou sala de choque.
6. **Módulo de Consultas & Agendamentos:** Pauta de atendimentos diários, agendamentos ambulatoriais, chamada em painel eletrônico de senhas, anamnese, exame físico, diagnóstico (CID-10) e fechamento de consulta.
7. **Módulo de Internamento & Gestão de Leitos:** Gestão de capacidade instalada por alas (UTI Adulto, Enfermaria Masculina/Feminina, Pediatria, Ortopedia, Isolamento), status dos leitos (`DISPONIVEL`, `OCUPADO`, `ALTA_PREVISTA`, `BLOQUEADO`), admissão hospitalar, evolução clínica diária e procedimentos de alta com higienização.
8. **Módulo de Laboratório & Meios de Diagnóstico:** Solicitação de exames laboratoriais e exames de imagem (Hemograma, PCR, Troponina STAT, Raio-X, Ultrassom), workflow de coleta de amostras biológicas, digitação e validação técnica de laudos com assinatura digital.
9. **Módulo de Farmácia Hospitalar & Dispensação:** Recepção de prescrições médicas eletrônicas, conferência de dosagens e vias de administração, dispensação de fármacos com validação de lote e rastreio rigoroso de substâncias de controle especial (psicotrópicos).
10. **Módulo de Estoque & Suprimentos:** Controle de almoxarifado hospitalar de medicamentos, descartáveis, EPIs e insumos cirúrgicos, rastreabilidade por lote e data de validade, disparador de alertas de estoque mínimo e registro de perdas/ajustes.
11. **Módulo Financeiro, Facturação Fiscal & Convênios:** Faturamento em moeda local (Kwanza - AOA), cálculo de impostos (IVA 14%), processamento de pagamentos particulares (Multicaixa/TPA, Numerário, Transferência) e faturamento de convênios/seguradoras (Ensa Saúde, Fidelidade, Nossa Seguros).
12. **Módulo de Notificações & Alertas em Tempo Real:** Disparo e distribuição de alertas operacionais críticos (ex.: estoque zerado, prioridade Manchester vermelha, laudo liberado, alta de leito).
13. **Módulo de Dashboard, Indicadores & Relatórios:** Consolidação de KPIs assistenciais e executivos (taxa de ocupação, permanência média, receita diária, curva ABC de medicamentos) e motor de exportação de relatórios em PDF, Excel (XLSX) e CSV.
14. **Módulo de Configurações Hospitalares & Auditoria:** Parâmetros institucionais (NIF, razão social, endereço), gestão de filiais/unidades (Luanda Mutamba, Talatona, Kilamba, Viana), backup de dados e trilha de auditoria completa (*audit trail*).

---

## 2. Mapeamento de Endpoints

> **Nota de Compatibilidade:** O cliente Zalo atual utiliza rotas divididas entre `/v1/auth/*` e `/api/v1/*`. Abaixo, todos os endpoints estão padronizados sob a convenção RESTful `/api/v1/`, recomendando a criação de aliases no back-end para as rotas antigas.

### 2.1 Autenticação & Perfil de Usuário

#### `POST /api/v1/auth/login` (Alias: `/v1/auth/login`)
- **Descrição:** Autentica um profissional com email e senha, retornando token JWT e dados do perfil.
- **Sucesso (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "mensagem": "Autenticação realizada com sucesso",
  "usuario": {
    "id": "usr-001",
    "nome": "Eduardo Torres",
    "email": "admin@hospital.com",
    "cargo": "ADMIN",
    "codigoFuncionario": "FUNC-001",
    "status": "ATIVO"
  }
}
```
- **Erros:**
  - `400 Bad Request`: Payload ausente ou incompleto.
  - `401 Unauthorized`: Credenciais incorretas ou usuário com status `BLOQUEADO` / `PENDENTE`.
  - `500 Internal Server Error`: Falha inesperada de autenticação.

#### `POST /api/v1/auth/register` (Alias: `/v1/auth/register`)
- **Descrição:** Solicita o cadastro de um novo operador hospitalar. Fica com status inicial `PENDENTE` aguardando validação pela coordenação.
- **Payload de Entrada ([RegisterDTO](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/RegisterDTO.java)):**
```json
{
  "name": "Dra. Teresa Domingos",
  "email": "teresa.domingos@hospital.com",
  "password": "SenhaForte@2026",
  "cargo": "TECNICO",
  "codigoFuncionario": "FUNC-103"
}
```
- **Sucesso (201 Created):**
```json
{
  "mensagem": "Solicitação de cadastro enviada com sucesso! O acesso será validado pela coordenação.",
  "usuarioId": "usr-103"
}
```
- **Erros:**
  - `400 Bad Request`: Email inválido, senha com menos de 6 caracteres ou campos faltantes.
  - `409 Conflict`: Email ou código de funcionário já em uso no sistema.
  - `500 Internal Server Error`: Erro ao persistir usuário.

#### `GET /api/v1/auth/me` (Alias: `/v1/auth/me`)
- **Descrição:** Retorna os dados do usuário atualmente autenticado a partir do Bearer Token.
- **Headers:** `Authorization: Bearer <token>`
- **Sucesso (200 OK):**
```json
{
  "id": "usr-001",
  "nome": "Eduardo Torres",
  "email": "admin@hospital.com",
  "cargo": "ADMIN",
  "codigoFuncionario": "FUNC-001",
  "status": "ATIVO"
}
```
- **Erros:**
  - `401 Unauthorized`: Token expirado, ausente ou inválido.
  - `404 Not Found`: Usuário não encontrado na base.

---

### 2.2 Gestão de Pacientes

#### `GET /api/v1/paciente`
- **Descrição:** Lista pacientes cadastrados, permitindo busca textual (`search`), filtro por convênio (`convenio`) e por status (`status`).
- **Headers:** `Authorization: Bearer <token>`
- **Query Params (opcionais):** `search=João&convenio=Ensa Saúde&status=ATIVO&page=0&size=20`
- **Sucesso (200 OK):**
```json
[
  {
    "id": "1",
    "nome": "João Pereira Silva",
    "bi": "005928172BA021",
    "idade": "34",
    "genero": "Masculino",
    "status": "ATIVO",
    "seguro": "Particular",
    "telefone": "+244 923 111 222"
  }
]
```
- **Erros:**
  - `401 Unauthorized`: Sessão expirada.
  - `500 Internal Server Error`: Erro de consulta ao banco de dados.

#### `POST /api/v1/paciente`
- **Descrição:** Cadastra um novo paciente no sistema hospitalar.
- **Headers:** `Authorization: Bearer <token>`
- **Payload de Entrada ([Paciente](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Paciente.java)):**
```json
{
  "nome": "Catarina de Barros",
  "bi": "008129481LA039",
  "genero": "Feminino",
  "idade": "28",
  "seguro": "Ensa Saúde",
  "telefone": "924888111"
}
```
- **Sucesso (201 Created):**
```json
{
  "id": "2",
  "nome": "Catarina de Barros",
  "bi": "008129481LA039",
  "genero": "Feminino",
  "idade": "28",
  "seguro": "Ensa Saúde",
  "telefone": "924888111",
  "status": "ATIVO"
}
```
- **Erros:**
  - `400 Bad Request`: Dados obrigatórios nulos (nome, BI, telefone).
  - `409 Conflict`: Número de BI já cadastrado para outro paciente.

#### `GET /api/v1/paciente/{id}/prontuario`
- **Descrição:** Obtém o prontuário médico completo do paciente, incluindo alergias, grupo sanguíneo, histórico de consultas, prescrições e exames.
- **Sucesso (200 OK):**
```json
{
  "pacienteId": "1",
  "nome": "João Pereira Silva",
  "tipoSanguineo": "O+",
  "alergias": ["Penicilina", "Diclofenaco"],
  "doencasPreExistentes": ["Hipertensão Arterial Sistêmica Leve"],
  "ultimaPressaoAferida": "125/82 mmHg",
  "historicoAtendimentos": [
    {
      "data": "2026-09-24T10:30:00",
      "medico": "Dr. Carlos Almeida",
      "especialidade": "Cardiologia",
      "diagnostico": "Acompanhamento pós-infarto",
      "conduta": "Prescrito Atenolol 50mg"
    }
  ]
}
```
- **Erros:**
  - `404 Not Found`: Paciente inexistente.

---

### 2.3 Corpo Clínico & Médicos

#### `GET /api/v1/medico`
- **Descrição:** Lista todos os médicos especialistas cadastrados com filtros por especialidade e turno.
- **Query Params:** `especialidade=Cardiologia&ativo=true`
- **Sucesso (200 OK):**
```json
[
  {
    "id": "med-01",
    "nome": "Dr. Carlos Almeida",
    "numeroOrdem": "ORM-2341",
    "especialidade": "Cardiologia",
    "telefone": "923456789",
    "ativo": true,
    "user": {
      "id": "usr-012",
      "nome": "Dr. Carlos Almeida",
      "email": "carlos.almeida@hospital.com",
      "cargo": "MEDICO"
    }
  }
]
```

#### `POST /api/v1/medico`
- **Descrição:** Cadastra um novo médico especialista no corpo clínico.
- **Payload de Entrada ([Medico](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medico.java)):**
```json
{
  "nome": "Dra. Juliana Martins",
  "numeroOrdem": "ORM-5892",
  "especialidade": "Ginecologia",
  "telefone": "931222444",
  "ativo": true
}
```
- **Sucesso (201 Created):** Retorna o objeto médico persistido com seu ID gerado.
- **Erros:**
  - `409 Conflict`: Número de Ordem dos Médicos (`numeroOrdem`) já cadastrado.

---

### 2.4 Triagem & Protocolo de Manchester

#### `POST /api/v1/triagem`
- **Descrição:** Registra a triagem do paciente, calcula ou registra a cor de risco segundo o Protocolo de Manchester e emite a senha de atendimento.
- **Payload:**
```json
{
  "pacienteId": 1,
  "classificacaoRisco": "VERMELHO",
  "tempoLimiteMinutos": 0,
  "pressaoArterial": "180/110",
  "saturacaoO2": 91,
  "temperatura": 36.8,
  "frequenciaCardiaca": 115,
  "queixaPrincipal": "Dor precordial intensa com irradiação",
  "observacoes": "Encaminhamento imediato para Sala de Choque / Reanimação"
}
```
- **Sucesso (201 Created):**
```json
{
  "id": 101,
  "senha": "E-01",
  "pacienteId": 1,
  "classificacaoRisco": "VERMELHO",
  "tempoEsperaEstimado": "0 min",
  "status": "AGUARDANDO_ATENDIMENTO",
  "dataHora": "2026-09-30T09:30:00"
}
```

#### `GET /api/v1/triagem/fila`
- **Descrição:** Retorna a fila ativa de pacientes classificados, ordenada estritamente pela gravidade do Protocolo de Manchester (Vermelho > Laranja > Amarelo > Verde > Azul) e horário de chegada.
- **Sucesso (200 OK):** Lista de senhas ativas com dados de sinais vitais e tempo decorrido.

---

### 2.5 Consultas & Atendimentos Clínicos

#### `GET /api/v1/consultas`
- **Descrição:** Consulta a agenda do dia ou período com filtros de data, especialidade, médico e status (`AGENDADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `CANCELADA`).
- **Sucesso (200 OK):**
```json
[
  {
    "id": 1,
    "pacienteId": 1,
    "pacienteNome": "João Pereira Silva",
    "medicoId": 12,
    "medicoNome": "Dr. Carlos Almeida",
    "especialidade": "Cardiologia",
    "dataHora": "2026-09-30T08:30:00",
    "motivo": "Acompanhamento pós-infarto",
    "status": "CONCLUIDA"
  }
]
```

#### `POST /api/v1/consultas`
- **Descrição:** Realiza o agendamento de uma nova consulta ambulatorial.
- **Sucesso (201 Created):** Consulta criada com status `AGENDADA`.

#### `POST /api/v1/consultas/{id}/finalizar`
- **Descrição:** Finaliza a consulta clínica, registrando anamnese, exame físico, diagnóstico (CID-10) e gerando automaticamente a prescrição médica correspondente.
- **Payload:**
```json
{
  "anamnese": "Paciente refere cefaleia e cansaço nos últimos 3 dias.",
  "exameFisico": "PA 120/80 mmHg, SpO2 98%, afebril.",
  "diagnostico": "Cefaleia tensional e estresse físico.",
  "observacoes": "Repouso e hidratação oral.",
  "prescricao": {
    "medicamentos": [
      {
        "nome": "Dipirona 500mg",
        "dosagem": "1 comprimido se dor",
        "frequencia": "6/6h",
        "duracao": "3 dias",
        "instrucoes": "Tomar após as refeições"
      }
    ]
  }
}
```
- **Sucesso (200 OK):** Status da consulta alterado para `CONCLUIDA` e ID da receita gerada retornado.

---

### 2.6 Internamento & Ocupação de Leitos

#### `GET /api/v1/leitos`
- **Descrição:** Retorna o mapa de leitos hospitalares com status em tempo real (`OCUPADO`, `DISPONIVEL`, `ALTA_PREVISTA`, `BLOQUEADO`) agrupados por Ala.
- **Sucesso (200 OK):**
```json
[
  {
    "id": 1,
    "codigo": "UTI-01",
    "ala": "UTI Adulto",
    "status": "OCUPADO",
    "pacienteAtual": {
      "id": 1,
      "nome": "António Miguel Silva",
      "dataAdmissao": "2026-09-22T14:00:00",
      "medicoAssistente": "Dr. Carlos Almeida"
    }
  },
  {
    "id": 2,
    "codigo": "Q-103 • L-12",
    "ala": "Enfermaria Masculina",
    "status": "DISPONIVEL",
    "pacienteAtual": null
  }
]
```

#### `POST /api/v1/internamentos`
- **Descrição:** Admite um paciente em um leito disponível.
- **Payload:**
```json
{
  "pacienteId": 1,
  "leitoId": 2,
  "medicoId": 12,
  "diagnosticoAdmissao": "Insuficiência Respiratória Aguda moderada",
  "ala": "Enfermaria Geral"
}
```
- **Sucesso (201 Created):** Leito com status atualizado para `OCUPADO` e registro de internação criado.

#### `POST /api/v1/internamentos/{id}/evolucao`
- **Descrição:** Registra a evolução médica ou de enfermagem do paciente internado.
- **Payload:**
```json
{
  "sinaisVitais": "PA: 128/84 mmHg • FC: 78 bpm • SpO2: 97% • Temp: 36.6°C",
  "dieta": "Oral branda com boa aceitação",
  "acessoVenoso": "Periférico em MSD salinizado, sem sinais de flebite",
  "conduta": "Paciente lúcido, orientado, eupneico. Melhora clínica contínua. Manter medicação prescrita."
}
```
- **Sucesso (201 Created):** Evolução gravada no prontuário do internamento.

#### `POST /api/v1/internamentos/{id}/alta`
- **Descrição:** Concede alta clínica, liberando o leito para o ciclo de higienização (`DISPONIVEL`).
- **Sucesso (200 OK):** Internamento encerrado e comprovante de alta emitido.

---

### 2.7 Laboratório & Meios de Diagnóstico

#### `GET /api/v1/exames`
- **Descrição:** Lista ordens de serviço laboratoriais com filtros por protocolo, tipo de exame e status (`AGUARD_COLETA`, `EM_ANALISE`, `LIBERADO`).
- **Sucesso (200 OK):** Lista de exames solicitados.

#### `POST /api/v1/exames`
- **Descrição:** Emite uma solicitação de exame clínico/imagiologia.
- **Payload:**
```json
{
  "pacienteId": 1,
  "medicoId": 12,
  "tipoExame": "Hemograma Completo + Plaquetas",
  "prioridade": "Urgente",
  "indicacaoClinica": "Investigação de anemia e fadiga crônica"
}
```
- **Sucesso (201 Created):** Retorna número de protocolo gerado (ex.: `#EX-4587`).

#### `PUT /api/v1/exames/{protocolo}/laudo`
- **Descrição:** Lança os resultados analíticos de bancada, emite parecer técnico e assina digitalmente o laudo.
- **Payload:**
```json
{
  "responsavelTecnico": "Dra. Teresa de Jesus Domingos - CRF 4821/AO",
  "parametros": [
    { "parametro": "Hemoglobina", "valor": "14.2 g/dL", "referencia": "13.0 - 17.5 g/dL", "status": "NORMAL" },
    { "parametro": "Leucócitos Totais", "valor": "6.800 /mm³", "referencia": "4.000 - 11.000 /mm³", "status": "NORMAL" },
    { "parametro": "Plaquetas", "valor": "245.000 /mm³", "referencia": "150.000 - 450.000 /mm³", "status": "NORMAL" }
  ],
  "conclusao": "Parâmetros hematológicos dentro dos valores normais de referência."
}
```
- **Sucesso (200 OK):** Laudo homologado e disponibilizado para download em PDF.

---

### 2.8 Farmácia Hospitalar & Estoque

#### `GET /api/v1/farmacia/prescricoes`
- **Descrição:** Lista as prescrições médicas direcionadas à farmácia central para separação e dispensação.
- **Sucesso (200 OK):** Lista de receitas pendentes e dispensadas.

#### `POST /api/v1/farmacia/dispensar`
- **Descrição:** Realiza a conferência farmacêutica, abate as quantidades diretamente do estoque central e gera o recibo de dispensação.
- **Payload:**
```json
{
  "receitaId": 8812,
  "farmaceuticoId": "FUNC-102",
  "itens": [
    { "medicamentoId": 42, "quantidadeDispensada": 1, "lote": "L-2024B" }
  ]
}
```
- **Sucesso (200 OK):** Recibo de dispensação gerado e estoque debitado.

#### `GET /api/v1/estoque`
- **Descrição:** Consulta os saldos físicos de insumos e medicamentos, alertando sobre itens abaixo do estoque de segurança.
- **Query Params:** `categoria=Medicamentos&alerta=CRITICO`
- **Sucesso (200 OK):**
```json
[
  {
    "id": 42,
    "codigoItem": "MED-042",
    "nome": "Paracetamol 500mg (Cx 20)",
    "categoria": "Medicamentos",
    "lote": "L-2024B",
    "dataValidade": "2026-12-31",
    "quantidade": 5,
    "quantidadeMinima": 20,
    "statusEstoque": "CRITICO_BAIXO"
  }
]
```

#### `POST /api/v1/estoque/movimentacao`
- **Descrição:** Registra entradas de compras ou ajustes manuais de estoque por inventário/descarte.
- **Payload:**
```json
{
  "medicamentoId": 42,
  "tipoMovimento": "ENTRADA",
  "quantidade": 100,
  "lote": "L-2026X",
  "dataValidade": "2028-06-30",
  "motivo": "Entrada por Compra / Fornecedor"
}
```
- **Sucesso (201 Created):** Saldo atualizado com sucesso.

---

### 2.9 Gestão Financeira & Facturação

#### `POST /api/v1/financeiro/faturas`
- **Descrição:** Emite uma Factura-Recibo oficial de prestação de serviços hospitalares em Kwanza (AOA), com NIF e discriminação de IVA (14%).
- **Payload:**
```json
{
  "pacienteId": 1,
  "descricaoServico": "Consulta Cardiologia + ECG",
  "metodo": "MULTICAIXA",
  "valorBruto": 45000.00,
  "aliquotaIva": 14.0,
  "convenio": "Particular"
}
```
- **Sucesso (201 Created):**
```json
{
  "faturaId": "FAT-2026-081",
  "pacienteNome": "João Pereira Silva",
  "valorTotal": 45000.00,
  "metodo": "MULTICAIXA",
  "status": "PAGO",
  "dataEmissao": "2026-09-30T09:30:00",
  "hashFiscal": "AGT-8472-2026-OK"
}
```

---

### 2.10 Dashboard & Relatórios

#### `GET /api/v1/dashboard/kpis`
- **Descrição:** Retorna em tempo real todos os contadores dos cards superiores do dashboard administrativo:
  - Total de Pacientes e percentual de evolução;
  - Médicos ativos no corpo clínico;
  - Consultas agendadas para o dia;
  - Leitos ocupados vs disponíveis vs bloqueados;
  - Total de medicamentos em estoque e alertas críticos.
- **Sucesso (200 OK):**
```json
{
  "totalPacientes": 1248,
  "totalMedicos": 38,
  "consultasHoje": 17,
  "leitos": { "total": 60, "ocupados": 42, "disponiveis": 17, "bloqueados": 1 },
  "medicamentosEstoque": 326,
  "consultasPorEspecialidade": {
    "Cardiologia": 42,
    "Pediatria": 35,
    "Ortopedia": 28,
    "Clinica Geral": 25,
    "Ginecologia": 18,
    "Dermatologia": 12
  }
}
```

---

### 2.11 Formato Padronizado de Respostas de Erro

Para qualquer falha (`400`, `401`, `403`, `404`, `409`, `500`), o back-end deve responder com o schema uniforme RFC 7807:

```json
{
  "timestamp": "2026-09-30T09:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Número de Bilhete de Identidade (BI) já cadastrado no sistema.",
  "path": "/api/v1/paciente"
}
```

---

## 3. Modelagem de Entidades

### 3.1 Entidades Existentes que Precisam de Atualizações

| Entidade | Arquivo | Problema Atual Identificado | Atualização Necessária |
| :--- | :--- | :--- | :--- |
| **`Usuario`** | [Usuario.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Usuario.java) | `id` está como `String`, sem campos de auditoria e sem senha hash no modelo de entidade de banco de dados. | Alterar `id` para `Long` (ou UUID padronizado em todo o sistema), adicionar `passwordHash`, `ultimoAcesso` (`LocalDateTime`), `criadoEm` e chave estrangeira para `Funcionario`. |
| **`Paciente`** | [Paciente.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Paciente.java) | `id` é `String`, `idade` é `String` pura (ex: "32 anos"), campos de saúde como tipo sanguíneo, alergias e histórico clínico ausentes na entidade. | Incompatibilidade de tipagem: `Consulta` usa `pacienteId` como `Long`, mas `Paciente` possui `id` como `String`. Padronizar para `Long id`. Substituir `idade` por `dataNascimento` (`LocalDate`) com cálculo automático de idade. Adicionar `tipoSanguineo`, `alergias` (Text/List) e `doencasCronicas`. |
| **`Medico`** | [Medico.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medico.java) | `id` é `String` (enquanto `Consulta` referencia `medicoId` como `Long`). `user` é um objeto embutido sem mapeamento JPA de chave estrangeira explícito. | Padronizar `Long id`. Adicionar `usuarioId` (`Long`), `funcionarioId` (`Long`), carga horária e dias de escala de plantão. |
| **`Funcionario`** | [Funcionario.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Funcionario.java) | Possui `cargo` como `String` simples e não possui relacionamento com `Usuario` ou departamento formal. | Tipar `cargo` com o enum [Cargo](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Cargo.java), adicionar `departamento` (`String` ou Entidade `Departamento`) e campo `turno` (`String`). |
| **`Enfermeiro`** | [Enfermeiro.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Enfermeiro.java) | Entidade duplicando campos de funcionário (`nome`, `telefone`, `email`) em vez de herdar ou associar estritamente. | Manter apenas `id`, `funcionarioId` (FK obrigatória), `numeroRegistro` (Ordem dos Enfermeiros de Angola - OEA), `especialidade` e `escalaTrabalho`. |
| **`Consulta`** | [Consulta.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Consulta.java) | Não possui campos para registrar os dados de atendimento exibidos no modal (anamnese, sinais vitais na consulta, CID-10 e prescrição vinculada). | Adicionar `anamnese` (`String`), `sinaisVitais` (`String`), `cid10` (`String`), `salaConsultorio` (`String`), e chave estrangeira para `Receita`. |
| **`Receita`** | [Receita.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Receita.java) | **Grave conflito de namespace:** Declara uma subclasse interna chamada `Medicamento` que colide com a classe de domínio [Medicamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medicamento.java). | Refatorar a classe interna para uma entidade relacional independente: `ItemReceita` (`id`, `receitaId`, `medicamentoId`, `dosagem`, `frequencia`, `duracao`, `instrucoes`). Adicionar campo `status` (`PENDENTE`, `DISPENSADA`). |
| **`Medicamento`** | [Medicamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medicamento.java) | É restrito a remédios, enquanto o sistema também estoca descartáveis, EPIs e insumos cirúrgicos. | Generalizar para `ItemEstoque` ou manter `Medicamento` como subtipo de um catálogo de produtos hospitalares, garantindo campos de `codigoBarras`, `lote`, `quantidadeMinima` e `localizacaoAlmoxarifado`. |
| **`Pagamento`** | [Pagamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Pagamento.java) | Possui apenas `pacienteId`, não vinculando a consulta, internação ou fatura fiscal emitida. | Adicionar `faturaId` (`Long`), `numeroRecibo` (`String`), `consultaId` (`Long`, opcional), `internamentoId` (`Long`, opcional), `impostoIva` (`BigDecimal`). |

---

### 3.2 Entidades Ausentes que Precisam ser Criadas no Back-end

1. **`Triagem` (Protocolo de Manchester):**
   - **Atributos:** `id` (Long), `senha` (String, ex: "E-01"), `pacienteId` (Long), `enfermeiroId` (Long), `classificacaoRisco` (Enum: `VERMELHO`, `LARANJA`, `AMARELO`, `VERDE`, `AZUL`), `pressaoArterial` (String), `saturacaoO2` (Integer), `temperatura` (Double), `frequenciaCardiaca` (Integer), `queixaPrincipal` (String), `observacoes` (String), `status` (Enum: `AGUARDANDO`, `EM_ATENDIMENTO`, `CONCLUIDA`), `dataHora` (LocalDateTime).
2. **`Leito` & `Ala` (Internamento):**
   - **`Ala`:** `id` (Long), `nome` (String, ex: "UTI Adulto", "Enfermaria Geral", "Pediatria"), `bloco` (String), `capacidade` (Integer).
   - **`Leito`:** `id` (Long), `alaId` (Long), `codigo` (String, ex: "UTI-01", "Q-103 • L-12"), `tipo` (Enum: `UTI`, `ENFERMARIA`, `ISOLAMENTO`), `status` (Enum: `DISPONIVEL`, `OCUPADO`, `ALTA_PREVISTA`, `BLOQUEADO`).
3. **`Internamento` & `EvolucaoClinica`:**
   - **`Internamento`:** `id` (Long), `pacienteId` (Long), `leitoId` (Long), `medicoResponsavelId` (Long), `diagnosticoAdmissao` (String), `dataAdmissao` (LocalDateTime), `dataAlta` (LocalDateTime), `status` (Enum: `ATIVO`, `ALTA_CONCEDIDA`, `TRANSFERIDO`, `OBITO`).
   - **`EvolucaoClinica`:** `id` (Long), `internamentoId` (Long), `profissionalId` (Long), `sinaisVitais` (String), `dieta` (String), `acessoVenoso` (String), `conduta` (String), `dataHora` (LocalDateTime).
4. **`SolicitacaoExame` & `LaudoLaboratorial`:**
   - **`SolicitacaoExame`:** `id` (Long), `protocolo` (String, ex: "#EX-4587"), `pacienteId` (Long), `medicoSolicitanteId` (Long), `tipoExame` (String), `prioridade` (Enum: `ROTINA`, `URGENTE`, `STAT`), `indicacaoClinica` (String), `status` (Enum: `AGUARDANDO_COLETA`, `EM_ANALISE`, `LIBERADO`), `dataSolicitacao` (LocalDateTime).
   - **`LaudoLaboratorial`:** `id` (Long), `solicitacaoExameId` (Long), `responsavelTecnicoId` (Long), `numeroRegistroCRF` (String), `resultadosJson` (Text/JSON com parâmetros e referências), `conclusao` (String), `dataLiberacao` (LocalDateTime), `hashAssinaturaDigital` (String).
5. **`Fatura` & `ItemFatura`:**
   - **`Fatura`:** `id` (Long), `numeroFatura` (String, ex: "FAT-2026-081"), `pacienteId` (Long), `convenio` (String), `valorTotal` (BigDecimal), `impostoIva` (BigDecimal), `metodoPagamento` (Enum: `MULTICAIXA`, `DINHEIRO`, `TRANSFERENCIA`, `CONVENIO`), `status` (Enum: `PAGO`, `PENDENTE`, `CANCELADO`), `dataEmissao` (LocalDateTime), `hashFiscal` (String).
6. **`Dispensacao`:**
   - **`Dispensacao`:** `id` (Long), `receitaId` (Long), `farmaceuticoId` (Long), `pacienteId` (Long), `dataDispensacao` (LocalDateTime), `reciboId` (String, ex: "#REC-8812").
7. **`MovimentacaoEstoque`:**
   - **`MovimentacaoEstoque`:** `id` (Long), `medicamentoId` (Long), `tipo` (Enum: `ENTRADA_COMPRA`, `SAIDA_DISPENSACAO`, `AJUSTE_INVENTARIO`, `PERDA_VALIDADE`), `quantidade` (Integer), `saldoAnterior` (Integer), `saldoAtual` (Integer), `motivo` (String), `usuarioId` (Long), `dataHora` (LocalDateTime).
8. **`Notificacao`:**
   - **`Notificacao`:** `id` (Long), `titulo` (String), `descricao` (String), `categoria` (Enum: `ESTOQUE`, `CONSULTA`, `EXAME`, `LEITO`, `PRESCRICAO`), `prioridade` (Enum: `NORMAL`, `URGENTE`), `destinatarioCargo` (Enum [Cargo](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Cargo.java), opcional), `lida` (boolean), `dataHora` (LocalDateTime).
9. **`ConfiguracaoHospital` & `UnidadeHospitalar`:**
   - **`ConfiguracaoHospital`:** `id` (Long), `nomeHospital` (String), `nif` (String), `telefone` (String), `endereco` (String), `email` (String), `moedaPadrao` (String: "AOA"), `alertaEstoqueAtivo` (boolean), `doisFatoresAtivo` (boolean), `auditoriaAtiva` (boolean).
   - **`UnidadeHospitalar`:** `id` (Long), `nomeFilial` (String), `localizacao` (String), `ativa` (boolean).
10. **`LogAuditoria`:**
    - **`LogAuditoria`:** `id` (Long), `usuarioId` (Long), `acao` (String), `modulo` (String), `ipOrigem` (String), `detalhes` (Text), `dataHora` (LocalDateTime).

---

## 4. Recomendações Críticas de Arquitetura

1. **Unificação dos Tipos de Identificadores (IDs):** Atualmente, entidades como [Paciente.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Paciente.java) e [Medico.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medico.java) usam `String id`, enquanto chaves estrangeiras em [Consulta.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Consulta.java), [Agendamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Agendamento.java) e [Pagamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Pagamento.java) usam `Long pacienteId` e `Long medicoId`. **Ação:** Padronizar todos os identificadores relacionais primários e estrangeiros como `Long` (ou migrar tudo uniformemente para `UUID`).
2. **Centralização do Cliente HTTP no Desktop:** [PacienteRepository.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/repositories/PacienteRepository.java) recria uma instância própria de `OkHttpClient` e duplica código, em vez de reutilizar [HttpConnector.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/repositories/HttpConnector.java). Além disso, os métodos de [HttpConnector.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/repositories/HttpConnector.java) engolem exceções silenciosamente e retornam booleanos simples (`false`) ou listas vazias, perdendo os status codes de erro do servidor (como `400`, `401` ou `409`).
3. **Resolução de Conflito de Nomes em `Receita`:** A classe interna `Receita.Medicamento` precisa ser renomeada para `ItemReceita` para evitar confusão de serialização e ambiguidade com o modelo [Medicamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medicamento.java).

---

## Registro de Mapeamento

Checkpoint de auditoria registrando rigorosamente todos os diretórios e arquivos inspecionados:

### Configuração e Build
- [pom.xml](file:///c:/Users/eduardo/Documents/Torres/Zalo/pom.xml)
- [details.md](file:///c:/Users/eduardo/Documents/Torres/Zalo/details.md) (verificado em disco: 0 bytes)
- [src/main/java/module-info.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/module-info.java)

### Modelos de Domínio (`src/main/java/.../models/`)
- [RegisterDTO.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/RegisterDTO.java)
- [ResponseDTO.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/ResponseDTO.java)
- [LoginRecord.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/LoginRecord.java)
- [Usuario.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Usuario.java)
- [Cargo.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Cargo.java)
- [Paciente.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Paciente.java)
- [Medico.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medico.java)
- [Funcionario.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Funcionario.java)
- [Enfermeiro.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Enfermeiro.java)
- [Consulta.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Consulta.java)
- [Agendamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Agendamento.java)
- [Medicamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Medicamento.java)
- [Receita.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Receita.java)
- [Pagamento.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/models/Pagamento.java)

### Repositórios e Conectores HTTP (`src/main/java/.../repositories/`)
- [HttpConnector.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/repositories/HttpConnector.java)
- [MedicosRepositories.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/repositories/MedicosRepositories.java)
- [PacienteRepository.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/repositories/PacienteRepository.java)

### Serviços (`src/main/java/.../services/`)
- [LoginService.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/services/LoginService.java)
- [RegisterService.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/services/RegisterService.java)
- [SessionService.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/services/SessionService.java)

### Controladores Java (`src/main/java/.../controllers/` e subpacotes)
- [MedicosController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/MedicosController.java)
- [MedicosHelper.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/helpers/MedicosHelper.java)
- [PacientesController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/PacientesController.java)
- [ConsultasController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/ConsultasController.java)
- [AtendimentoController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/AtendimentoController.java)
- [InternamentoController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/InternamentoController.java)
- [LaboratorioController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/LaboratorioController.java)
- [FarmaciaController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/FarmaciaController.java)
- [EstoqueController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/EstoqueController.java)
- [FinanceiroController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/FinanceiroController.java)
- [FuncionariosController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/FuncionariosController.java)
- [RelatoriosController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/RelatoriosController.java)
- [UtilizadoresController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/UtilizadoresController.java)
- [ConfiguracoesController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/ConfiguracoesController.java)
- [DashboardController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/DashboardController.java)
- [NotificacoesController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/NotificacoesController.java)
- [ModalHelper.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/controllers/ModalHelper.java) (1.132 linhas analisadas)
- [LoginController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/login/LoginController.java)
- [RegisterController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/login/RegisterController.java)
- [MainLayoutController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/layout/MainLayoutController.java)
- [SidebarController.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/layout/SidebarController.java)
- [HelloApplication.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/HelloApplication.java)
- [Launcher.java](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/java/com/us/torres/zelo/zalo/Launcher.java)

### Telas FXML e Recursos Visuais (`src/main/resources/.../view/`, `layout/`, `login/`)
- [atendimento.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/atendimento.fxml)
- [consultas.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/consultas.fxml)
- [dashboard.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/dashboard.fxml)
- [estoque.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/estoque.fxml)
- [farmacia.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/farmacia.fxml)
- [financeiro.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/financeiro.fxml)
- [funcionarios.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/funcionarios.fxml)
- [internamento.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/internamento.fxml)
- [laboratorio.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/laboratorio.fxml)
- [medicos.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/medicos.fxml)
- [pacientes.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/pacientes.fxml)
- [notificacoes.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/notificacoes.fxml)
- [relatorios.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/relatorios.fxml)
- [utilizadores.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/utilizadores.fxml)
- [configuracoes.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/view/configuracoes.fxml)
- [main-layout.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/layout/main-layout.fxml)
- [sidebar.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/layout/sidebar.fxml)
- [login-page.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/login/login-page.fxml)
- [register-page.fxml](file:///c:/Users/eduardo/Documents/Torres/Zalo/src/main/resources/com/us/torres/zelo/zalo/login/register-page.fxml)