package com.us.Torres.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A aplicação não cria dados fictícios automaticamente.
 *
 * Todos os utilizadores, pacientes, médicos, leitos, medicamentos e demais
 * configurações devem ser registados através dos fluxos reais da aplicação
 * ou disponibilizados pelo ambiente/base de dados configurado.
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedInitialData() {
        return args -> {
            // Intencionalmente vazio: não inserir dados estáticos/fictícios.
        };
    }
}
