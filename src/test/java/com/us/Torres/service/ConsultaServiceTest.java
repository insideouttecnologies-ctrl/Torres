package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.Consulta;
import com.us.Torres.models.Medico;
import com.us.Torres.models.Paciente;
import com.us.Torres.repository.ConsultaRepository;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultaServiceTest {

    @Test
    void shouldCreateConsultaSuccessfully() {
        ConsultaRepository consultaRepository = mock(ConsultaRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);

        ConsultaService consultaService = new ConsultaService(consultaRepository, pacienteRepository, medicoRepository);

        Paciente paciente = Paciente.builder()
                .id("pac-1")
                .nome("Maria Alves")
                .build();

        Medico medico = Medico.builder()
                .id("med-1")
                .nome("Dr. João")
                .especialidade("Cardiologia")
                .build();

        Consulta consulta = Consulta.builder()
                .pacienteId("pac-1")
                .medicoId("med-1")
                .motivo("Dor no peito")
                .build();

        when(pacienteRepository.findById("pac-1")).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById("med-1")).thenReturn(Optional.of(medico));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Consulta salvo = consultaService.criar(consulta);

        assertNotNull(salvo);
        assertEquals("Maria Alves", salvo.getPacienteNome());
        assertEquals("Dr. João", salvo.getMedicoNome());
        assertEquals("Cardiologia", salvo.getEspecialidade());
        assertEquals(Consulta.StatusConsulta.AGENDADA, salvo.getStatus());
    }

    @Test
    void shouldRejectConsultaWithoutPaciente() {
        ConsultaRepository consultaRepository = mock(ConsultaRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);

        ConsultaService consultaService = new ConsultaService(consultaRepository, pacienteRepository, medicoRepository);

        Consulta consulta = Consulta.builder()
                .medicoId("med-1")
                .motivo("Dor no peito")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> consultaService.criar(consulta));
        assertEquals("Paciente é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldRejectConsultaWithoutDoctor() {
        ConsultaRepository consultaRepository = mock(ConsultaRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);

        ConsultaService consultaService = new ConsultaService(consultaRepository, pacienteRepository, medicoRepository);

        Consulta consulta = Consulta.builder()
                .pacienteId("pac-1")
                .motivo("Dor no peito")
                .build();

        when(pacienteRepository.findById("pac-1")).thenReturn(Optional.of(Paciente.builder().id("pac-1").nome("Maria").build()));

        BusinessException ex = assertThrows(BusinessException.class, () -> consultaService.criar(consulta));
        assertEquals("Médico é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldRejectInvalidTransitionFromCompletedConsulta() {
        ConsultaRepository consultaRepository = mock(ConsultaRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);
        ConsultaService consultaService = new ConsultaService(consultaRepository, pacienteRepository, medicoRepository);

        Consulta consulta = Consulta.builder()
                .id("c-1")
                .pacienteId("pac-1")
                .medicoId("med-1")
                .motivo("Dor no peito")
                .status(Consulta.StatusConsulta.CONCLUIDA)
                .build();

        when(consultaRepository.findById("c-1")).thenReturn(Optional.of(consulta));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> consultaService.atualizarStatus("c-1", Consulta.StatusConsulta.AGENDADA));

        assertTrue(ex.getMessage().contains("transição"));
    }
}
