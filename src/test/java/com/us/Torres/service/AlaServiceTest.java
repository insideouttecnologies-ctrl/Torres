package com.us.Torres.service;

import com.us.Torres.models.Ala;
import com.us.Torres.repository.AlaRepository;
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
class AlaServiceTest {

    @Mock
    private AlaRepository alaRepository;

    @InjectMocks
    private AlaService alaService;

    @Test
    void deveListarAlaPorNome() {
        Ala ala = Ala.builder()
                .id("ala-1")
                .nome("UTI Adulto")
                .bloco("Bloco A")
                .capacidade(12)
                .build();

        when(alaRepository.findAllByOrderByNomeAsc()).thenReturn(List.of(ala));

        List<Ala> resultado = alaService.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals("UTI Adulto", resultado.get(0).getNome());
    }

    @Test
    void deveCriarAlaComDadosValidos() {
        Ala ala = Ala.builder()
                .nome("Enfermaria Masculina")
                .bloco("Bloco B")
                .capacidade(18)
                .build();

        when(alaRepository.existsByNome("Enfermaria Masculina")).thenReturn(false);
        when(alaRepository.save(ala)).thenReturn(ala);

        Ala criada = alaService.criar(ala);

        assertNotNull(criada);
        assertEquals("Enfermaria Masculina", criada.getNome());
    }

    @Test
    void deveBuscarAlaPorId() {
        Ala ala = Ala.builder().id("ala-2").nome("Pediatria").build();
        when(alaRepository.findById("ala-2")).thenReturn(Optional.of(ala));

        Ala encontrada = alaService.buscarPorId("ala-2");

        assertEquals("Pediatria", encontrada.getNome());
    }
}
