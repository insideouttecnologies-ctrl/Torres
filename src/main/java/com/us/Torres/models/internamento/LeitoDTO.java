package com.us.Torres.models.internamento;

import com.us.Torres.models.Leito;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeitoDTO {
    private String id;
    private String codigo;
    private String ala;
    private Leito.StatusLeito status;
    private PacienteAtualDTO pacienteAtual;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacienteAtualDTO {
        private String id;
        private String nome;
        private LocalDateTime dataAdmissao;
        private String medicoAssistente;
    }
}
