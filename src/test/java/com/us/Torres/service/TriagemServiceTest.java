package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.Triagem;
import com.us.Torres.repository.PacienteRepository;
import com.us.Torres.repository.TriagemRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TriagemServiceTest {

    @Test
    void shouldSafelyListFilaWhenTriagemHasNullRisk() {
        TriagemRepository triagemRepository = mock(TriagemRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        TriagemService triagemService = new TriagemService(triagemRepository, pacienteRepository);

        Triagem triagemSemRisco = Triagem.builder()
                .status(Triagem.StatusTriagem.AGUARDANDO_ATENDIMENTO)
                .pacienteId("pac-1")
                .pacienteNome("João")
                .build();

        when(triagemRepository.findAll()).thenReturn(List.of(triagemSemRisco));

        assertDoesNotThrow(triagemService::listarFila);
    }

    @Test
    void shouldRejectInvalidTransitionFromCompletedTriagem() {
        TriagemRepository triagemRepository = mock(TriagemRepository.class);
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        TriagemService triagemService = new TriagemService(triagemRepository, pacienteRepository);

        Triagem triagem = Triagem.builder()
                .id("tri-1")
                .pacienteId("pac-1")
                .status(Triagem.StatusTriagem.CONCLUIDA)
                .classificacaoRisco(Triagem.ClassificacaoRisco.AMARELO)
                .build();

        when(triagemRepository.findById("tri-1")).thenReturn(java.util.Optional.of(triagem));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> triagemService.atualizarStatus("tri-1", Triagem.StatusTriagem.EM_ATENDIMENTO));

        assertTrue(ex.getMessage().contains("transição"));
    }
}
