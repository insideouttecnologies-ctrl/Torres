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
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            seedAdmin(userRepository, passwordEncoder);
            seedMedicos(userRepository, medicoRepository, passwordEncoder);
            seedLeitos(leitoRepository);
            seedPacientes(pacienteRepository);
            seedMedicamentos(medicamentoRepository);
            seedConfiguracao(configuracaoHospitalRepository);
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
}
