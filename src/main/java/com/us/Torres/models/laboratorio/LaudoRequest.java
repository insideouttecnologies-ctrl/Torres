package com.us.Torres.models.laboratorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaudoRequest {
    private String responsavelTecnico;
    private String numeroRegistroCRF;
    private List<ParametroLaudoDTO> parametros;
    private String conclusao;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParametroLaudoDTO {
        private String parametro;
        private String valor;
        private String referencia;
        private String status;
    }
}
