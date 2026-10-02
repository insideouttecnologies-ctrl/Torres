package com.us.Torres.service;

import com.us.Torres.models.Leito;
import com.us.Torres.repository.LeitoRepository;
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
class LeitoServiceTest {

    @Mock
    private LeitoRepository leitoRepository;

    @InjectMocks
    private LeitoService leitoService;

    @Test
    void deveListarLeitosPorCodigo() {
        Leito leito = Leito.builder()
                .id("lt-1")
                .codigo("UTI-01")
                .ala("UTI Adulto")
                .status(Leito.StatusLeito.DISPONIVEL)
                .build();

        when(leitoRepository.findAllByOrderByCodigoAsc()).thenReturn(List.of(leito));

        List<Leito> resultado = leitoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("UTI-01", resultado.get(0).getCodigo());
    }

    @Test
    void deveCriarLeitoComDadosValidos() {
        Leito leito = Leito.builder()
                .codigo("Q-103")
                .ala("Enfermaria Masculina")
                .status(Leito.StatusLeito.DISPONIVEL)
                .build();

        when(leitoRepository.existsByCodigo("Q-103")).thenReturn(false);
        when(leitoRepository.save(leito)).thenReturn(leito);

        Leito salvo = leitoService.criar(leito);

        assertNotNull(salvo);
        assertEquals("Q-103", salvo.getCodigo());
    }

    @Test
    void deveBuscarLeitoPorId() {
        Leito leito = Leito.builder().id("lt-2").codigo("L-12").build();
        when(leitoRepository.findById("lt-2")).thenReturn(Optional.of(leito));

        Leito encontrado = leitoService.buscarPorId("lt-2");

        assertEquals("L-12", encontrado.getCodigo());
    }
}
