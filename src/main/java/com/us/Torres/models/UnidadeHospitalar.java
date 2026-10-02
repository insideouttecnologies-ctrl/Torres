package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unidade_hospitalar")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeHospitalar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nomeFilial;

    private String localizacao;

    @Builder.Default
    private boolean ativa = true;
}
