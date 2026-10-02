package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.*;
import com.us.Torres.models.internamento.InternamentoRequest;
import com.us.Torres.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InternamentoServiceTest {

    @Mock
    private InternamentoRepository internamentoRepository;

    @Mock
    private LeitoRepository leitoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EvolucaoClinicaRepository evolucaoClinicaRepository;

    @InjectMocks
    private InternamentoService internamentoService;

    @Test
    void deveBloquearAdmissaoQuandoPacienteJaPossuiInternamentoAtivo() {
        Paciente paciente = Paciente.builder()
                .id("p-1")
                .nome("Maria")
                .bi("123456")
                .status("ATIVO")
                .build();

        Leito leito = Leito.builder()
                .id("l-1")
                .codigo("UTI-01")
                .ala("UTI Adulto")
                .status(Leito.StatusLeito.DISPONIVEL)
                .build();

        Internamento internamentoAtivo = Internamento.builder()
                .id("i-1")
                .pacienteId("p-1")
                .status(Internamento.StatusInternamento.ATIVO)
                .build();

        when(pacienteRepository.findById("p-1")).thenReturn(Optional.of(paciente));
        when(internamentoRepository.findFirstByPacienteIdAndStatusOrderByDataAdmissaoDesc("p-1", Internamento.StatusInternamento.ATIVO))
                .thenReturn(Optional.of(internamentoAtivo));

        InternamentoRequest request = InternamentoRequest.builder()
                .pacienteId("p-1")
                .leitoId("l-1")
                .medicoId("m-1")
                .diagnosticoAdmissao("Dengue grave")
                .ala("UTI Adulto")
                .build();

        assertThrows(BusinessException.class, () -> internamentoService.admitirPaciente(request));
    }
}
