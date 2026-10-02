package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ala")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ala {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String nome;

    private String bloco;

    private Integer capacidade;
}
