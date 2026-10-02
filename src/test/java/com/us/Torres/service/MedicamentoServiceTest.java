package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.Medicamento;
import com.us.Torres.repository.MedicamentoRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicamentoServiceTest {

    @Test
    void shouldCreateMedicamentoSuccessfully() {
        MedicamentoRepository repository = mock(MedicamentoRepository.class);
        MedicamentoService service = new MedicamentoService(repository);

        Medicamento medicamento = Medicamento.builder()
                .codigoItem("MED-001")
                .nome("Paracetamol")
                .principioAtivo("Acetaminofeno")
                .categoria("Analgesico")
                .fabricante("Farmacia X")
                .quantidade(25)
                .quantidadeMinima(10)
                .precoCompra(new BigDecimal("2.50"))
                .precoVenda(new BigDecimal("4.90"))
                .build();

        when(repository.findByCodigoItem("MED-001")).thenReturn(Optional.empty());
        when(repository.save(any(Medicamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Medicamento salvo = service.criar(medicamento);

        assertNotNull(salvo);
        assertEquals("MED-001", salvo.getCodigoItem());
        assertTrue(salvo.isAtivo());
        assertEquals("NORMAL", salvo.getStatusEstoque());
    }

    @Test
    void shouldRejectMedicamentoWithoutName() {
        MedicamentoRepository repository = mock(MedicamentoRepository.class);
        MedicamentoService service = new MedicamentoService(repository);

        Medicamento medicamento = Medicamento.builder()
                .codigoItem("MED-002")
                .quantidade(5)
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> service.criar(medicamento));
        assertEquals("Nome do medicamento é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldRejectNegativeStock() {
        MedicamentoRepository repository = mock(MedicamentoRepository.class);
        MedicamentoService service = new MedicamentoService(repository);

        Medicamento medicamento = Medicamento.builder()
                .codigoItem("MED-003")
                .nome("Ibuprofeno")
                .quantidade(-1)
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> service.criar(medicamento));
        assertEquals("Quantidade em estoque não pode ser negativa.", ex.getMessage());
    }
}
