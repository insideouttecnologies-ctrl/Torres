package com.us.Torres.controllers;

import com.us.Torres.models.EvolucaoClinica;
import com.us.Torres.models.Internamento;
import com.us.Torres.models.internamento.EvolucaoRequest;
import com.us.Torres.models.internamento.InternamentoRequest;
import com.us.Torres.service.InternamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class InternamentoController {

    private final InternamentoService internamentoService;

    @PostMapping("/internamentos")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Internamento> admitirPaciente(@RequestBody InternamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(internamentoService.admitirPaciente(request));
    }

    @PostMapping("/internamentos/{id}/evolucao")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<EvolucaoClinica> registrarEvolucao(@PathVariable String id, @RequestBody EvolucaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(internamentoService.registrarEvolucao(id, request));
    }

    @PostMapping("/internamentos/{id}/alta")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Internamento> darAlta(@PathVariable String id) {
        return ResponseEntity.ok(internamentoService.darAlta(id));
    }

    @PatchMapping("/internamentos/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Internamento> atualizarStatus(@PathVariable String id, @RequestParam Internamento.StatusInternamento status) {
        return ResponseEntity.ok(internamentoService.atualizarStatus(id, status));
    }

    @PostMapping("/internamentos/{id}/transferir")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Internamento> transferirPaciente(@PathVariable String id,
                                                         @RequestParam String novoLeitoId) {
        return ResponseEntity.ok(internamentoService.transferirPaciente(id, novoLeitoId));
    }
}
