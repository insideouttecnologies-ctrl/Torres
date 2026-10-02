package com.us.Torres.models.pep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProntuarioDTO {
    private String pacienteId;
    private String nome;
    private String tipoSanguineo;
    private List<String> alergias;
    private List<String> doencasPreExistentes;
    private String ultimaPressaoAferida;
    private List<HistoricoAtendimentoDTO> historicoAtendimentos;
}
