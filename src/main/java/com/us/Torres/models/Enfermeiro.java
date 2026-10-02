package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "enfermeiro")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enfermeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String funcionarioId;

    private String nome;

    @Column(unique = true)
    private String numeroRegistro;

    private String especialidade;

    private String telefone;

    private String email;

    private String escalaTrabalho;

    private boolean ativo;
}