package com.us.Torres.service;

import com.us.Torres.repository.AgendamentoRepository;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AgendamentoServiceTest {

    @Test
    void shouldCreateServiceInstance() {
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);

        AgendamentoService agendamentoService = new AgendamentoService(agendamentoRepository, pacienteRepository, medicoRepository);

        assertNotNull(agendamentoService);
        assertNotNull(agendamentoRepository);
        assertNotNull(pacienteRepository);
        assertNotNull(medicoRepository);
    }
}
