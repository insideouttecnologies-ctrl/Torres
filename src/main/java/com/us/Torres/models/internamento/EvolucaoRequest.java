package com.us.Torres.models.internamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolucaoRequest {
    private String profissionalId;
    private String profissionalNome;
    private String sinaisVitais;
    private String dieta;
    private String acessoVenoso;
    private String conduta;
}
