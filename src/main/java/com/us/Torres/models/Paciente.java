package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    @Column(unique = true)
    private String bi;

    private String genero;

    private String idade;

    private LocalDate dataNascimento;

    private String status;

    private String seguro;

    @Column(unique = true)
    private String telefone;

    private String tipoSanguineo;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(columnDefinition = "TEXT")
    private String doencasPreExistentes;

    private String ultimaPressaoAferida;

    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        if (status == null || status.isBlank()) {
            status = "ATIVO";
        }
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}