package com.us.Torres.config;

import com.us.Torres.models.ConfiguracaoHospital;
import com.us.Torres.models.Leito;
import com.us.Torres.models.Medicamento;
import com.us.Torres.models.Medico;
import com.us.Torres.models.Paciente;
import com.us.Torres.models.users.Cargo;
import com.us.Torres.models.users.User;
import com.us.Torres.repository.ConfiguracaoHospitalRepository;
import com.us.Torres.repository.LeitoRepository;
import com.us.Torres.repository.MedicamentoRepository;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import com.us.Torres.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedInitialData(
            UserRepository userRepository,
            MedicoRepository medicoRepository,
            LeitoRepository leitoRepository,
            PacienteRepository pacienteRepository,
            MedicamentoRepository medicamentoRepository,
            ConfiguracaoHospitalRepository configuracaoHospitalRepository,
            com.us.Torres.repository.ReceitaRepository receitaRepository,
            com.us.Torres.repository.FuncionarioRepository funcionarioRepository,
            com.us.Torres.repository.SolicitacaoExameRepository solicitacaoExameRepository,
            com.us.Torres.repository.LaudoLaboratorialRepository laudoLaboratorialRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            seedAdmin(userRepository, passwordEncoder);
            seedMedicos(userRepository, medicoRepository, passwordEncoder);
            seedLeitos(leitoRepository);
            seedPacientes(pacienteRepository);
            seedMedicamentos(medicamentoRepository);
            seedConfiguracao(configuracaoHospitalRepository);
            seedReceitas(receitaRepository, pacienteRepository, medicoRepository, medicamentoRepository);
            seedFuncionarios(funcionarioRepository);
            seedExames(solicitacaoExameRepository, laudoLaboratorialRepository, pacienteRepository, medicoRepository);
        };
    }

    private void seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.findByEmail("admin@hospital.com").isEmpty()) {
            User admin = new User(
                    "Eduardo Torres",
                    "admin@hospital.com",
                    passwordEncoder.encode("SenhaForte@2026"),
                    Cargo.ADMIN,
                    "FUNC-001"
            );
            admin.setStatus(User.StatusUsuario.ATIVO);
            userRepository.save(admin);
        }
    }

    private void seedMedicos(UserRepository userRepository, MedicoRepository medicoRepository, PasswordEncoder passwordEncoder) {
        List<String[]> medicosBase = List.of(
                new String[]{"Dr. Ana Costa", "ana.costa@hospital.com", "MED-001", "Cardiologia", "923000001", "MANHA"},
                new String[]{"Dr. Bruno Silva", "bruno.silva@hospital.com", "MED-002", "Ortopedia", "923000002", "TARDE"},
                new String[]{"Dra. Carla Mendes", "carla.mendes@hospital.com", "MED-003", "Pediatria", "923000003", "MANHA"}
        );

        for (String[] dados : medicosBase) {
            String nome = dados[0];
            String email = dados[1];
            String codigoFuncionario = dados[2];
            String especialidade = dados[3];
            String telefone = dados[4];
            String escala = dados[5];

            if (userRepository.findByEmail(email).isEmpty()) {
                User user = new User(nome, email, passwordEncoder.encode("SenhaForte@2026"), Cargo.MEDICO, codigoFuncionario);
                user.setStatus(User.StatusUsuario.ATIVO);
                userRepository.save(user);
            }

            if (medicoRepository.findByNumeroOrdem(codigoFuncionario).isEmpty()) {
                User medicoUser = userRepository.findByEmail(email).orElseThrow();
                Medico medico = Medico.builder()
                        .nome(nome)
                        .numeroOrdem(codigoFuncionario)
                        .especialidade(especialidade)
                        .telefone(telefone)
                        .ativo(true)
                        .user(medicoUser)
                        .funcionarioId(medicoUser.getId())
                        .escala(escala)
                        .build();
                medicoRepository.save(medico);
            }
        }
    }

    private void seedLeitos(LeitoRepository leitoRepository) {
        List<String[]> leitosBase = List.of(
                new String[]{"UTI-01", "UTI Adulto", "DISPONIVEL"},
                new String[]{"UTI-02", "UTI Adulto", "OCUPADO"},
                new String[]{"ENF-101", "Enfermaria A", "DISPONIVEL"},
                new String[]{"ENF-102", "Enfermaria A", "OCUPADO"},
                new String[]{"ENF-201", "Enfermaria B", "DISPONIVEL"},
                new String[]{"Q-301", "Quarto Individual", "DISPONIVEL"}
        );

        for (String[] dados : leitosBase) {
            String codigo = dados[0];
            String ala = dados[1];
            String status = dados[2];

            if (leitoRepository.findByCodigo(codigo).isEmpty()) {
                Leito leito = Leito.builder()
                        .codigo(codigo)
                        .ala(ala)
                        .status(Leito.StatusLeito.valueOf(status))
                        .build();
                leitoRepository.save(leito);
            }
        }
    }

    private void seedPacientes(PacienteRepository pacienteRepository) {
        List<Paciente> pacientesBase = List.of(
                Paciente.builder()
                        .nome("Maria António")
                        .bi("000111222LA1")
                        .genero("FEMININO")
                        .idade("42")
                        .dataNascimento(LocalDate.of(1983, 4, 18))
                        .status("ATIVO")
                        .seguro("Particular")
                        .telefone("923000010")
                        .tipoSanguineo("A+")
                        .alergias("Penicilina")
                        .doencasPreExistentes("Hipertensão")
                        .ultimaPressaoAferida("120/80")
                        .build(),
                Paciente.builder()
                        .nome("João Pedro")
                        .bi("000111223LA1")
                        .genero("MASCULINO")
                        .idade("33")
                        .dataNascimento(LocalDate.of(1992, 8, 12))
                        .status("ATIVO")
                        .seguro("SAS")
                        .telefone("923000011")
                        .tipoSanguineo("O-")
                        .alergias("Nenhuma")
                        .doencasPreExistentes("Asma")
                        .ultimaPressaoAferida("118/76")
                        .build(),
                Paciente.builder()
                        .nome("Sofia Nunes")
                        .bi("000111224LA1")
                        .genero("FEMININO")
                        .idade("16")
                        .dataNascimento(LocalDate.of(2009, 2, 9))
                        .status("ATIVO")
                        .seguro("Particular")
                        .telefone("923000012")
                        .tipoSanguineo("B+")
                        .alergias("Nenhuma")
                        .doencasPreExistentes("Nenhuma")
                        .ultimaPressaoAferida("110/70")
                        .build(),
                Paciente.builder()
                        .nome("Rui Tavares")
                        .bi("000111225LA1")
                        .genero("MASCULINO")
                        .idade("58")
                        .dataNascimento(LocalDate.of(1967, 10, 25))
                        .status("ATIVO")
                        .seguro("Hospital")
                        .telefone("923000013")
                        .tipoSanguineo("AB+")
                        .alergias("Ibuprofeno")
                        .doencasPreExistentes("Diabetes")
                        .ultimaPressaoAferida("128/82")
                        .build()
        );

        for (Paciente paciente : pacientesBase) {
            if (pacienteRepository.findByBi(paciente.getBi()).isEmpty()) {
                pacienteRepository.save(paciente);
            }
        }
    }

    private void seedMedicamentos(MedicamentoRepository medicamentoRepository) {
        List<Medicamento> medicamentosBase = List.of(
                Medicamento.builder()
                        .codigoItem("MED-001")
                        .nome("Paracetamol 500mg")
                        .principioAtivo("Paracetamol")
                        .categoria("Analgesico")
                        .fabricante("Farmanguela")
                        .codigoBarras("750100000001")
                        .lote("LOT-PAR-2026")
                        .quantidade(120)
                        .quantidadeMinima(25)
                        .precoCompra(new BigDecimal("7.50"))
                        .precoVenda(new BigDecimal("12.00"))
                        .dosagem("500mg")
                        .formaFarmaceutica("Comprimido")
                        .localizacaoAlmoxarifado("A1-01")
                        .controlado(false)
                        .ativo(true)
                        .build(),
                Medicamento.builder()
                        .codigoItem("MED-002")
                        .nome("Amoxicilina 250mg")
                        .principioAtivo("Amoxicilina")
                        .categoria("Antibiótico")
                        .fabricante("Medipharm")
                        .codigoBarras("750100000002")
                        .lote("LOT-AMO-2026")
                        .quantidade(18)
                        .quantidadeMinima(20)
                        .precoCompra(new BigDecimal("24.00"))
                        .precoVenda(new BigDecimal("39.90"))
                        .dosagem("250mg")
                        .formaFarmaceutica("Comprimido")
                        .localizacaoAlmoxarifado("A2-03")
                        .controlado(false)
                        .ativo(true)
                        .build(),
                Medicamento.builder()
                        .codigoItem("MED-003")
                        .nome("Insulina Regular")
                        .principioAtivo("Insulina")
                        .categoria("Hormonal")
                        .fabricante("NovoDia")
                        .codigoBarras("750100000003")
                        .lote("LOT-INS-2026")
                        .quantidade(10)
                        .quantidadeMinima(12)
                        .precoCompra(new BigDecimal("85.00"))
                        .precoVenda(new BigDecimal("130.00"))
                        .dosagem("100UI/ml")
                        .formaFarmaceutica("Frasco")
                        .localizacaoAlmoxarifado("B1-05")
                        .controlado(true)
                        .ativo(true)
                        .build()
        );

        for (Medicamento medicamento : medicamentosBase) {
            medicamento.calcularStatusEstoque();
            if (medicamentoRepository.findByCodigoItem(medicamento.getCodigoItem()).isEmpty()) {
                medicamentoRepository.save(medicamento);
            }
        }
    }

    private void seedConfiguracao(ConfiguracaoHospitalRepository configuracaoHospitalRepository) {
        if (configuracaoHospitalRepository.findFirstByOrderByAtualizadoEmDesc().isEmpty()) {
            ConfiguracaoHospital configuracao = ConfiguracaoHospital.builder()
                    .nomeHospital("Hospital Torres")
                    .nif("0000000000")
                    .telefone("+244 222 000 000")
                    .endereco("Rua da Medicina, 123, Luanda")
                    .email("contato@hospitaltorres.com")
                    .moedaPadrao("AOA")
                    .alertaEstoqueAtivo(true)
                    .doisFatoresAtivo(false)
                    .auditoriaAtiva(true)
                    .build();
            configuracaoHospitalRepository.save(configuracao);
        }
    }

    private void seedReceitas(
            com.us.Torres.repository.ReceitaRepository receitaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            MedicamentoRepository medicamentoRepository
    ) {
        if (receitaRepository.count() == 0) {
            List<Paciente> pacientes = pacienteRepository.findAll();
            List<Medico> medicos = medicoRepository.findAll();
            List<Medicamento> medicamentos = medicamentoRepository.findAll();

            if (!pacientes.isEmpty() && !medicos.isEmpty() && !medicamentos.isEmpty()) {
                Paciente p1 = pacientes.get(0);
                Paciente p2 = pacientes.size() > 1 ? pacientes.get(1) : p1;
                Medico m1 = medicos.get(0);
                Medicamento med1 = medicamentos.get(0);
                Medicamento med2 = medicamentos.size() > 1 ? medicamentos.get(1) : med1;

                com.us.Torres.models.Receita r1 = com.us.Torres.models.Receita.builder()
                        .pacienteId(p1.getId())
                        .medicoId(m1.getId())
                        .status(com.us.Torres.models.Receita.StatusReceita.PENDENTE)
                        .dataEmissao(LocalDate.now())
                        .observacoes("Tomar após as principais refeições.")
                        .itens(List.of(
                                com.us.Torres.models.Receita.ItemReceita.builder()
                                        .medicamentoId(med1.getId())
                                        .nome(med1.getNome())
                                        .dosagem(med1.getDosagem())
                                        .frequencia("8 em 8 horas")
                                        .duracao("5 dias")
                                        .quantidadeSolicitada(6)
                                        .instrucoes("Via Oral")
                                        .build()
                        ))
                        .build();

                com.us.Torres.models.Receita r2 = com.us.Torres.models.Receita.builder()
                        .pacienteId(p2.getId())
                        .medicoId(m1.getId())
                        .status(com.us.Torres.models.Receita.StatusReceita.PENDENTE)
                        .dataEmissao(LocalDate.now())
                        .observacoes("Tratamento de infecção respiratória.")
                        .itens(List.of(
                                com.us.Torres.models.Receita.ItemReceita.builder()
                                        .medicamentoId(med2.getId())
                                        .nome(med2.getNome())
                                        .dosagem(med2.getDosagem())
                                        .frequencia("12 em 12 horas")
                                        .duracao("7 dias")
                                        .quantidadeSolicitada(4)
                                        .instrucoes("Via Oral com bastante água")
                                        .build()
                        ))
                        .build();

                receitaRepository.saveAll(List.of(r1, r2));
            }
        }
    }

    private void seedFuncionarios(com.us.Torres.repository.FuncionarioRepository funcionarioRepository) {
        if (funcionarioRepository.count() == 0) {
            List<com.us.Torres.models.Funcionario> base = List.of(
                    com.us.Torres.models.Funcionario.builder()
                            .nome("Beatriz Fernandes")
                            .codigoFuncionario("FUNC-101")
                            .email("beatriz.fernandes@hospital.com")
                            .telefone("+244 923 111 001")
                            .endereco("Avenida 4 de Fevereiro, Luanda")
                            .cargo(Cargo.ENFERMEIRO)
                            .departamento("Enfermagem / UTI")
                            .turno("Plantão 12x36 (Diurno)")
                            .dataNascimento(LocalDate.of(1989, 6, 14))
                            .dataContratacao(LocalDate.of(2021, 3, 15))
                            .ativo(true)
                            .build(),
                    com.us.Torres.models.Funcionario.builder()
                            .nome("Manuel António Sebastião")
                            .codigoFuncionario("FUNC-102")
                            .email("manuel.sebastiao@hospital.com")
                            .telefone("+244 923 222 002")
                            .endereco("Bairro Alvalade, Luanda")
                            .cargo(Cargo.FARMACEUTICO)
                            .departamento("Farmácia Central")
                            .turno("Manhã (07h-15h)")
                            .dataNascimento(LocalDate.of(1985, 11, 20))
                            .dataContratacao(LocalDate.of(2019, 8, 1))
                            .ativo(true)
                            .build(),
                    com.us.Torres.models.Funcionario.builder()
                            .nome("Dra. Teresa Domingos")
                            .codigoFuncionario("FUNC-103")
                            .email("teresa.domingos@hospital.com")
                            .telefone("+244 923 333 003")
                            .endereco("Maianga, Luanda")
                            .cargo(Cargo.LABORATORISTA)
                            .departamento("Laboratório Clínico")
                            .turno("Manhã (07h-15h)")
                            .dataNascimento(LocalDate.of(1982, 3, 29))
                            .dataContratacao(LocalDate.of(2018, 5, 10))
                            .ativo(true)
                            .build(),
                    com.us.Torres.models.Funcionario.builder()
                            .nome("António Kiala")
                            .codigoFuncionario("FUNC-104")
                            .email("antonio.kiala@hospital.com")
                            .telefone("+244 923 444 004")
                            .endereco("Miramar, Luanda")
                            .cargo(Cargo.RECEPCIONISTA)
                            .departamento("Recepção & Triagem")
                            .turno("Tarde (13h-21h)")
                            .dataNascimento(LocalDate.of(1995, 9, 8))
                            .dataContratacao(LocalDate.of(2022, 1, 15))
                            .ativo(true)
                            .build(),
                    com.us.Torres.models.Funcionario.builder()
                            .nome("Cláudia Van-Dúnem")
                            .codigoFuncionario("FUNC-105")
                            .email("claudia.dunen@hospital.com")
                            .telefone("+244 923 555 005")
                            .endereco("Talatona, Luanda")
                            .cargo(Cargo.ADMIN)
                            .departamento("Financeiro & Convênios")
                            .turno("Comercial (08h-17h)")
                            .dataNascimento(LocalDate.of(1987, 12, 3))
                            .dataContratacao(LocalDate.of(2020, 2, 1))
                            .ativo(true)
                            .build(),
                    com.us.Torres.models.Funcionario.builder()
                            .nome("Lucas Morais")
                            .codigoFuncionario("FUNC-106")
                            .email("lucas.morais@hospital.com")
                            .telefone("+244 923 666 006")
                            .endereco("Ingombota, Luanda")
                            .cargo(Cargo.TECNICO)
                            .departamento("Laboratório Clínico")
                            .turno("Plantão 12x36 (Noturno)")
                            .dataNascimento(LocalDate.of(1993, 7, 19))
                            .dataContratacao(LocalDate.of(2023, 4, 10))
                            .ativo(true)
                            .build()
            );
            funcionarioRepository.saveAll(base);
        }
    }

    private void seedExames(
            com.us.Torres.repository.SolicitacaoExameRepository solicitacaoExameRepository,
            com.us.Torres.repository.LaudoLaboratorialRepository laudoLaboratorialRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository
    ) {
        if (solicitacaoExameRepository.count() == 0) {
            List<Paciente> pacientes = pacienteRepository.findAll();
            List<Medico> medicos = medicoRepository.findAll();

            if (!pacientes.isEmpty() && !medicos.isEmpty()) {
                Paciente p1 = pacientes.get(0);
                Paciente p2 = pacientes.size() > 1 ? pacientes.get(1) : p1;
                Paciente p3 = pacientes.size() > 2 ? pacientes.get(2) : p1;
                Paciente p4 = pacientes.size() > 3 ? pacientes.get(3) : p1;
                Medico m1 = medicos.get(0);
                Medico m2 = medicos.size() > 1 ? medicos.get(1) : m1;

                com.us.Torres.models.SolicitacaoExame ex1 = com.us.Torres.models.SolicitacaoExame.builder()
                        .protocolo("#EX-4587")
                        .pacienteId(p1.getId())
                        .pacienteNome(p1.getNome())
                        .medicoId(m1.getId())
                        .medicoNome(m1.getNome())
                        .tipoExame("Hemograma Completo + Plaquetas")
                        .prioridade(com.us.Torres.models.SolicitacaoExame.PrioridadeExame.ROTINA)
                        .indicacaoClinica("Check-up admissional clínico de rotina.")
                        .status(com.us.Torres.models.SolicitacaoExame.StatusExame.LIBERADO)
                        .dataSolicitacao(java.time.LocalDateTime.now().minusHours(4))
                        .build();

                com.us.Torres.models.SolicitacaoExame ex2 = com.us.Torres.models.SolicitacaoExame.builder()
                        .protocolo("#EX-4588")
                        .pacienteId(p2.getId())
                        .pacienteNome(p2.getNome())
                        .medicoId(m2.getId())
                        .medicoNome(m2.getNome())
                        .tipoExame("Proteína C Reativa (PCR)")
                        .prioridade(com.us.Torres.models.SolicitacaoExame.PrioridadeExame.URGENTE)
                        .indicacaoClinica("Febre persistente e suspeita de processo infeccioso agudo.")
                        .status(com.us.Torres.models.SolicitacaoExame.StatusExame.EM_ANALISE)
                        .dataSolicitacao(java.time.LocalDateTime.now().minusHours(2))
                        .build();

                com.us.Torres.models.SolicitacaoExame ex3 = com.us.Torres.models.SolicitacaoExame.builder()
                        .protocolo("#EX-4589")
                        .pacienteId(p3.getId())
                        .pacienteNome(p3.getNome())
                        .medicoId(m1.getId())
                        .medicoNome(m1.getNome())
                        .tipoExame("Glicemia em Jejum")
                        .prioridade(com.us.Torres.models.SolicitacaoExame.PrioridadeExame.ROTINA)
                        .indicacaoClinica("Acompanhamento semestral de paciente diabético.")
                        .status(com.us.Torres.models.SolicitacaoExame.StatusExame.AGUARDANDO_COLETA)
                        .dataSolicitacao(java.time.LocalDateTime.now().minusMinutes(50))
                        .build();

                com.us.Torres.models.SolicitacaoExame ex4 = com.us.Torres.models.SolicitacaoExame.builder()
                        .protocolo("#EX-4590")
                        .pacienteId(p4.getId())
                        .pacienteNome(p4.getNome())
                        .medicoId(m2.getId())
                        .medicoNome(m2.getNome())
                        .tipoExame("Troponina I Quantitativa")
                        .prioridade(com.us.Torres.models.SolicitacaoExame.PrioridadeExame.STAT)
                        .indicacaoClinica("Dor precordial aguda em aperto. Protocolo de dor torácica de emergência.")
                        .status(com.us.Torres.models.SolicitacaoExame.StatusExame.AGUARDANDO_COLETA)
                        .dataSolicitacao(java.time.LocalDateTime.now().minusMinutes(15))
                        .build();

                ex1 = solicitacaoExameRepository.save(ex1);
                solicitacaoExameRepository.saveAll(List.of(ex2, ex3, ex4));

                com.us.Torres.models.LaudoLaboratorial laudo = com.us.Torres.models.LaudoLaboratorial.builder()
                        .exameId(ex1.getId())
                        .protocolo(ex1.getProtocolo())
                        .responsavelTecnico("Dra. Teresa Domingos")
                        .numeroRegistroCRF("CRF-4821/AO")
                        .parametrosJson("[{\"parametro\":\"Hemoglobina\",\"valor\":\"14.8 g/dL\",\"referencia\":\"13.0 - 17.5 g/dL\",\"status\":\"NORMAL\"},{\"parametro\":\"Leucócitos Totais\",\"valor\":\"6.400 /mm³\",\"referencia\":\"4.000 - 11.000 /mm³\",\"status\":\"NORMAL\"},{\"parametro\":\"Plaquetas\",\"valor\":\"255.000 /mm³\",\"referencia\":\"150.000 - 450.000 /mm³\",\"status\":\"NORMAL\"},{\"parametro\":\"Hematócrito\",\"valor\":\"44.2 %\",\"referencia\":\"40 - 52 %\",\"status\":\"NORMAL\"}]")
                        .conclusao("Quadro hematológico dentro dos limites normais de referência para a faixa etária. Sem atipias morfológicas celulares observadas.")
                        .dataLiberacao(java.time.LocalDateTime.now().minusHours(1))
                        .hashAssinaturaDigital("LAUDO-SHA256-7F89B12D03")
                        .build();

                laudoLaboratorialRepository.save(laudo);
            }
        }
    }
}
