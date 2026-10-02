package com.us.Torres.models;

import com.us.Torres.models.users.Cargo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "funcionario")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    @Column(unique = true)
    private String codigoFuncionario;

    private String email;

    private String telefone;

    private String endereco;

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    private String departamento;

    private String turno;

    private LocalDate dataNascimento;

    private LocalDate dataContratacao;

    private boolean ativo;

    private String usuarioId;
}