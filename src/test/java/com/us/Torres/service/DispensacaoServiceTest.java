package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.Dispensacao;
import com.us.Torres.models.Medicamento;
import com.us.Torres.models.Receita;
import com.us.Torres.models.farmacia.DispensacaoRequest;
import com.us.Torres.repository.DispensacaoRepository;
import com.us.Torres.repository.MedicamentoRepository;
import com.us.Torres.repository.MovimentacaoEstoqueRepository;
import com.us.Torres.repository.ReceitaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispensacaoServiceTest {

    @Mock
    private DispensacaoRepository dispensacaoRepository;

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @InjectMocks
    private DispensacaoService dispensacaoService;

    @Test
    void deveRejeitarDispensacaoQuandoMedicamentoNaoPertenceAReceita() {
        Receita receita = Receita.builder()
                .id("rec-1")
                .pacienteId("pac-1")
                .medicoId("med-1")
                .status(Receita.StatusReceita.PENDENTE)
                .itens(List.of(Receita.ItemReceita.builder()
                        .medicamentoId("med-1")
                        .nome("Amoxicilina")
                        .quantidadeSolicitada(10)
                        .build()))
                .build();

        when(receitaRepository.findById("rec-1")).thenReturn(Optional.of(receita));

        DispensacaoRequest request = DispensacaoRequest.builder()
                .receitaId("rec-1")
                .farmaceuticoId("farm-1")
                .pacienteId("pac-1")
                .itens(List.of(DispensacaoRequest.ItemDispensacao.builder()
                        .medicamentoId("med-2")
                        .medicamentoNome("Ibuprofeno")
                        .quantidadeDispensada(5)
                        .build()))
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> dispensacaoService.dispensar(request));
        assertEquals("Medicamento não autorizado pela receita: Ibuprofeno", ex.getMessage());
    }

    @Test
    void deveSalvarHistoricoDeMovimentoAoDispensarMedicamentoValido() {
        Receita receita = Receita.builder()
                .id("rec-2")
                .pacienteId("pac-1")
                .medicoId("med-1")
                .status(Receita.StatusReceita.PENDENTE)
                .itens(List.of(Receita.ItemReceita.builder()
                        .medicamentoId("med-1")
                        .nome("Amoxicilina")
                        .quantidadeSolicitada(10)
                        .build()))
                .build();

        Medicamento medicamento = Medicamento.builder()
                .id("med-1")
                .nome("Amoxicilina")
                .quantidade(20)
                .quantidadeMinima(5)
                .build();

        when(receitaRepository.findById("rec-2")).thenReturn(Optional.of(receita));
        when(medicamentoRepository.findById("med-1")).thenReturn(Optional.of(medicamento));
        when(dispensacaoRepository.save(any(Dispensacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(movimentacaoEstoqueRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DispensacaoRequest request = DispensacaoRequest.builder()
                .receitaId("rec-2")
                .farmaceuticoId("farm-1")
                .pacienteId("pac-1")
                .itens(List.of(DispensacaoRequest.ItemDispensacao.builder()
                        .medicamentoId("med-1")
                        .medicamentoNome("Amoxicilina")
                        .quantidadeDispensada(5)
                        .build()))
                .build();

        Dispensacao resultado = dispensacaoService.dispensar(request);

        assertEquals("rec-2", resultado.getReceitaId());
        assertEquals(15, medicamento.getQuantidade());
    }
}
