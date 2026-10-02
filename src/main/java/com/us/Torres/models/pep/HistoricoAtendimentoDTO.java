package com.us.Torres.models.pep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoAtendimentoDTO {
    private LocalDateTime data;
    private String medico;
    private String especialidade;
    private String diagnostico;
    private String conduta;
}
