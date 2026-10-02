package com.us.Torres.controllers;

import com.us.Torres.models.EvolucaoClinica;
import com.us.Torres.models.internamento.EvolucaoRequest;
import com.us.Torres.service.EvolucaoClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class EvolucaoClinicaController {

    private final EvolucaoClinicaService evolucaoClinicaService;

    @GetMapping("/internamentos/{internamentoId}/evolucoes")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<List<EvolucaoClinica>> listarEvolucoes(@PathVariable String internamentoId) {
        return ResponseEntity.ok(evolucaoClinicaService.listarPorInternamento(internamentoId));
    }

    @PostMapping("/internamentos/{internamentoId}/evolucoes")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<EvolucaoClinica> registrarEvolucao(@PathVariable String internamentoId, @RequestBody EvolucaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evolucaoClinicaService.registrar(internamentoId, request));
    }
}
