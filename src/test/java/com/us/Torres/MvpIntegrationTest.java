package com.us.Torres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.us.Torres.models.*;
import com.us.Torres.models.financeiro.FaturaRequest;
import com.us.Torres.models.financeiro.PagamentoRequest;
import com.us.Torres.models.internamento.EvolucaoRequest;
import com.us.Torres.models.internamento.InternamentoRequest;
import com.us.Torres.models.farmacia.DispensacaoRequest;
import com.us.Torres.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvpIntegrationTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldAuthenticateAndAccessDashboardWithJwt() throws Exception {
        String token = loginAsAdmin();

        HttpResponse<String> response = doGet("/api/v1/dashboard/resumo", token);

        assertThat(response.statusCode()).isEqualTo(200);
        Map<String, Object> body = asMap(response.body());
        assertThat(body).containsKey("totalPacientes");
        assertThat(body).containsKey("totalConsultas");
        assertThat(body).containsKey("totalTriagens");
        assertThat(body).containsKey("admissoesAtivas");
    }

    @Test
    void shouldCreateAndUpdateConsulta() throws Exception {
        String token = loginAsAdmin();
        String pacienteId = ensurePaciente("Maria Consulta", "BI-CONS-001");
        String medicoId = ensureMedico("Dr. Consulta");

        Map<String, Object> payload = Map.of(
                "pacienteId", pacienteId,
                "medicoId", medicoId,
                "motivo", "Dor no peito",
                "salaConsultorio", "Sala 1",
                "especialidade", "Cardiologia"
        );

        HttpResponse<String> createResponse = doPost("/api/v1/consulta", token, payload);
        assertThat(createResponse.statusCode()).isEqualTo(201);
        Map<String, Object> created = asMap(createResponse.body());
        assertThat(created.get("pacienteId")).isEqualTo(pacienteId);
        assertThat(created.get("status")).isEqualTo("AGENDADA");

        String consultaId = (String) created.get("id");

        HttpResponse<String> updateResponse = doPatch("/api/v1/consulta/" + consultaId + "/status?status=EM_ANDAMENTO", token);
        assertThat(updateResponse.statusCode()).isEqualTo(200);
        Map<String, Object> updated = asMap(updateResponse.body());
        assertThat(updated.get("status")).isEqualTo("EM_ANDAMENTO");
    }

    @Test
    void shouldCreateAndUpdateTriagem() throws Exception {
        String token = loginAsAdmin();
        String pacienteId = ensurePaciente("Pedro Triagem", "BI-TRI-001");

        Map<String, Object> payload = Map.of(
                "pacienteId", pacienteId,
                "enfermeiroId", "enf-1",
                "classificacaoRisco", "VERMELHO",
                "queixaPrincipal", "Desmaio súbito",
                "pressaoArterial", "90/60",
                "frequenciaCardiaca", 120,
                "saturacaoO2", 90
        );

        HttpResponse<String> createResponse = doPost("/api/v1/triagem", token, payload);
        assertThat(createResponse.statusCode()).isEqualTo(201);
        Map<String, Object> created = asMap(createResponse.body());
        assertThat(created.get("pacienteId")).isEqualTo(pacienteId);
        assertThat(created.get("status")).isEqualTo("AGUARDANDO_ATENDIMENTO");

        String triagemId = (String) created.get("id");

        HttpResponse<String> updateResponse = doPatch("/api/v1/triagem/" + triagemId + "/status?status=EM_ATENDIMENTO", token);
        assertThat(updateResponse.statusCode()).isEqualTo(200);
        Map<String, Object> updated = asMap(updateResponse.body());
        assertThat(updated.get("status")).isEqualTo("EM_ATENDIMENTO");
    }

    @Test
    void shouldAdmitAndEvolvePatient() throws Exception {
        String token = loginAsAdmin();
        String pacienteId = ensurePaciente("Ana Internamento", "BI-INT-001");
        String medicoId = ensureMedico("Dr. Internamento");
        String leitoId = ensureLeito("INT-DELTA-01");

        InternamentoRequest request = InternamentoRequest.builder()
                .pacienteId(pacienteId)
                .leitoId(leitoId)
                .medicoId(medicoId)
                .diagnosticoAdmissao("Dor abdominal aguda")
                .ala("Enfermaria A")
                .build();

        HttpResponse<String> createResponse = doPost("/api/v1/internamentos", token, request);
        assertThat(createResponse.statusCode()).isEqualTo(201);
        Map<String, Object> created = asMap(createResponse.body());
        assertThat(created.get("pacienteId")).isEqualTo(pacienteId);
        assertThat(created.get("status")).isEqualTo("ATIVO");

        String internamentoId = (String) created.get("id");

        EvolucaoRequest evolucaoRequest = EvolucaoRequest.builder()
                .profissionalId(medicoId)
                .profissionalNome("Dr. Internamento")
                .sinaisVitais("PA 120/80, FC 88, Sat 97%")
                .dieta("Sem restrições")
                .acessoVenoso("Periférico direito")
                .conduta("Observação clínica")
                .build();

        HttpResponse<String> evolucaoResponse = doPost("/api/v1/internamentos/" + internamentoId + "/evolucao", token, evolucaoRequest);
        assertThat(evolucaoResponse.statusCode()).isEqualTo(201);
        Map<String, Object> evolucao = asMap(evolucaoResponse.body());
        assertThat(evolucao.get("internamentoId")).isEqualTo(internamentoId);

        HttpResponse<String> altaResponse = doPost("/api/v1/internamentos/" + internamentoId + "/alta", token, Map.of());
        assertThat(altaResponse.statusCode()).isEqualTo(200);
        Map<String, Object> alta = asMap(altaResponse.body());
        assertThat(alta.get("status")).isEqualTo("ALTA_CONCEDIDA");
    }

    @Test
    void shouldDispensePrescriptionAndRegisterFinancialPayment() throws Exception {
        String token = loginAsAdmin();
        String pacienteId = ensurePaciente("Paula Farmacia", "BI-FAR-001");
        String medicoId = ensureMedico("Dr. Farmacia");
        String medicamentoId = ensureMedicamento("Paracetamol 500mg", "MED-FAR-001", 40, 20);

        Receita receita = Receita.builder()
                .pacienteId(pacienteId)
                .medicoId(medicoId)
                .status(Receita.StatusReceita.PENDENTE)
                .itens(List.of(
                        Receita.ItemReceita.builder()
                                .medicamentoId(medicamentoId)
                                .nome("Paracetamol 500mg")
                                .quantidadeSolicitada(5)
                                .build()
                ))
                .build();

        receita = receitaRepository.save(receita);

        DispensacaoRequest request = DispensacaoRequest.builder()
                .receitaId(receita.getId())
                .farmaceuticoId("farm-1")
                .pacienteId(pacienteId)
                .observacao("Dispensa conforme prescrição")
                .itens(List.of(
                        DispensacaoRequest.ItemDispensacao.builder()
                                .medicamentoId(medicamentoId)
                                .medicamentoNome("Paracetamol 500mg")
                                .quantidadeDispensada(3)
                                .lote("LOT-PAR-2026")
                                .build()
                ))
                .build();

        HttpResponse<String> dispensarResponse = doPost("/api/v1/farmacia/dispensar", token, request);
        assertThat(dispensarResponse.statusCode()).isEqualTo(200);
        Map<String, Object> dispensacao = asMap(dispensarResponse.body());
        assertThat(dispensacao.get("receitaId")).isEqualTo(receita.getId());

        FaturaRequest faturaRequest = FaturaRequest.builder()
                .pacienteId(pacienteId)
                .descricao("Consulta + medicação")
                .convenio("Particular")
                .valorBruto(new BigDecimal("150.00"))
                .build();

        HttpResponse<String> faturaResponse = doPost("/api/v1/financeiro/faturas", token, faturaRequest);
        assertThat(faturaResponse.statusCode()).isEqualTo(201);
        Map<String, Object> fatura = asMap(faturaResponse.body());
        String faturaId = (String) fatura.get("id");

        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .faturaId(faturaId)
                .pacienteId(pacienteId)
                .valorPago(new BigDecimal("150.00"))
                .metodoPagamento("DINHEIRO")
                .descricao("Pagamento avulso")
                .build();

        HttpResponse<String> pagamentoResponse = doPost("/api/v1/financeiro/faturas/" + faturaId + "/pagamentos", token, pagamentoRequest);
        assertThat(pagamentoResponse.statusCode()).isEqualTo(201);
        Map<String, Object> pagamento = asMap(pagamentoResponse.body());
        assertThat(pagamento.get("faturaId")).isEqualTo(faturaId);
        assertThat(pagamento.get("status")).isEqualTo("PAGO");

        Medicamento medicamentoAtual = medicamentoRepository.findById(medicamentoId).orElseThrow();
        assertThat(medicamentoAtual.getQuantidade()).isEqualTo(37);
    }

    private String loginAsAdmin() throws Exception {
        Map<String, String> loginPayload = Map.of(
                "email", "admin@hospital.com",
                "password", "SenhaForte@2026"
        );

        HttpResponse<String> response = doPost("/api/v1/auth/login", null, loginPayload);
        assertThat(response.statusCode()).isEqualTo(200);
        Map<String, Object> loginBody = asMap(response.body());
        assertThat(loginBody.get("token")).isNotNull();
        return (String) loginBody.get("token");
    }

    private String ensurePaciente(String nome, String bi) {
        return pacienteRepository.findByBi(bi)
                .map(Paciente::getId)
                .orElseGet(() -> pacienteRepository.save(Paciente.builder()
                        .nome(nome)
                        .bi(bi)
                        .genero("MASCULINO")
                        .idade("30")
                        .dataNascimento(LocalDate.of(1995, 1, 10))
                        .status("ATIVO")
                        .telefone("923" + System.currentTimeMillis() % 1000000)
                        .seguro("Particular")
                        .build()).getId());
    }

    private String ensureMedico(String nome) {
        return medicoRepository.findAllByOrderByNomeAsc().stream()
                .filter(m -> m.getNome().equals(nome) || m.getEspecialidade() != null)
                .findFirst()
                .map(Medico::getId)
                .orElseGet(() -> medicoRepository.save(Medico.builder()
                        .nome(nome)
                        .numeroOrdem("MED-INT-" + System.currentTimeMillis())
                        .especialidade("Clínica Geral")
                        .telefone("923" + System.currentTimeMillis() % 1000000)
                        .ativo(true)
                        .build()).getId());
    }

    private String ensureLeito(String codigo) {
        return leitoRepository.findByCodigo(codigo)
                .map(Leito::getId)
                .orElseGet(() -> leitoRepository.save(Leito.builder()
                        .codigo(codigo)
                        .ala("Enfermaria A")
                        .status(Leito.StatusLeito.DISPONIVEL)
                        .build()).getId());
    }

    private String ensureMedicamento(String nome, String codigoItem, int quantidade, int quantidadeMinima) {
        return medicamentoRepository.findByCodigoItem(codigoItem)
                .map(Medicamento::getId)
                .orElseGet(() -> medicamentoRepository.save(Medicamento.builder()
                        .codigoItem(codigoItem)
                        .nome(nome)
                        .principioAtivo("Base")
                        .categoria("Analgesico")
                        .fabricante("Teste")
                        .codigoBarras(codigoItem)
                        .quantidade(quantidade)
                        .quantidadeMinima(quantidadeMinima)
                        .precoCompra(new BigDecimal("5.00"))
                        .precoVenda(new BigDecimal("10.00"))
                        .lote("LOT-TESTE")
                        .ativo(true)
                        .build()).getId());
    }

    private HttpResponse<String> doGet(String path, String token) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path));
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        HttpRequest request = builder.build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> doPost(String path, String token, Object body) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json");
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        if (body != null) {
            builder.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)));
        } else {
            builder.POST(HttpRequest.BodyPublishers.noBody());
        }

        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> doPatch(String path, String token) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.noBody());
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private Map<String, Object> asMap(String body) throws IOException {
        return objectMapper.readValue(body, Map.class);
    }
}
