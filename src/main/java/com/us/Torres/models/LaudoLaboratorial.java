package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "laudo_laboratorial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaudoLaboratorial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String exameId;

    @Column(nullable = false)
    private String protocolo;

    private String responsavelTecnico;

    private String numeroRegistroCRF;

    @Column(columnDefinition = "TEXT")
    private String parametrosJson;

    @Column(columnDefinition = "TEXT")
    private String conclusao;

    private LocalDateTime dataLiberacao;

    private String hashAssinaturaDigital;

    @PrePersist
    public void prePersist() {
        if (this.dataLiberacao == null) {
            this.dataLiberacao = LocalDateTime.now();
        }
    }
}
