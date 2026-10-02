package com.us.Torres.service;

import com.us.Torres.models.Medico;
import com.us.Torres.models.users.User;
import com.us.Torres.repository.MedicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private MedicoService medicoService;

    @Test
    void deveListarMedicosOrdenadosPorNome() {
        Medico medico = Medico.builder()
                .id("med-1")
                .nome("Dr. Ana Silva")
                .numeroOrdem("CRM-123")
                .especialidade("Cardiologia")
                .telefone("999999999")
                .ativo(true)
                .build();

        when(medicoRepository.findAllByOrderByNomeAsc()).thenReturn(List.of(medico));

        List<Medico> resultado = medicoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Dr. Ana Silva", resultado.get(0).getNome());
    }

    @Test
    void deveCriarMedicoComDadosValidos() {
        Medico medico = Medico.builder()
                .nome("Dr. Bruno Costa")
                .numeroOrdem("CRM-456")
                .especialidade("Neurologia")
                .telefone("988888888")
                .build();

        when(medicoRepository.existsByNumeroOrdem("CRM-456")).thenReturn(false);
        when(medicoRepository.existsByTelefone("988888888")).thenReturn(false);
        when(medicoRepository.save(medico)).thenReturn(medico);

        Medico salvo = medicoService.criar(medico);

        assertNotNull(salvo);
        assertTrue(salvo.isAtivo());
        verify(medicoRepository).save(medico);
    }

    @Test
    void deveBuscarMedicoPorId() {
        Medico medico = Medico.builder().id("med-2").nome("Dr. Carla Mendes").build();

        when(medicoRepository.findById("med-2")).thenReturn(Optional.of(medico));

        Medico encontrado = medicoService.buscarPorId("med-2");

        assertEquals("med-2", encontrado.getId());
    }
}
