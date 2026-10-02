package com.us.Torres.models.internamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternamentoRequest {
    private String pacienteId;
    private String leitoId;
    private String medicoId;
    private String diagnosticoAdmissao;
    private String ala;
}
