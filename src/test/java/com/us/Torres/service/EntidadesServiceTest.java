package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.models.Medico;
import com.us.Torres.models.Paciente;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntidadesServiceTest {

    @Test
    void pacienteServiceShouldRejectNullPatient() {
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        PacienteService pacienteService = new PacienteService(pacienteRepository);

        BusinessException ex = assertThrows(BusinessException.class, () -> pacienteService.salvar(null));

        assertEquals("Dados do paciente são obrigatórios.", ex.getMessage());
    }

    @Test
    void pacienteServiceShouldRejectEmptyRequiredFields() {
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        PacienteService pacienteService = new PacienteService(pacienteRepository);
        Paciente paciente = new Paciente();

        assertThrows(BusinessException.class, () -> pacienteService.salvar(paciente));
    }

    @Test
    void medicoServiceShouldRejectNullDoctor() {
        MedicoRepository medicoRepository = mock(MedicoRepository.class);
        MedicoService medicoService = new MedicoService(medicoRepository);

        BusinessException ex = assertThrows(BusinessException.class, () -> medicoService.criar(null));

        assertEquals("Dados do médico são obrigatórios.", ex.getMessage());
    }

    @Test
    void medicoServiceShouldRejectDuplicateOrdem() {
        MedicoRepository medicoRepository = mock(MedicoRepository.class);
        MedicoService medicoService = new MedicoService(medicoRepository);

        Medico medico = Medico.builder()
                .nome("Dr. João")
                .numeroOrdem("CRM-123")
                .especialidade("Cardiologia")
                .telefone("999999999")
                .build();

        when(medicoRepository.existsByNumeroOrdem("CRM-123")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class, () -> medicoService.criar(medico));

        assertEquals("Número da ordem do médico já cadastrado.", ex.getMessage());
    }
}

