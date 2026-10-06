package com.us.Torres.controllers;

import com.us.Torres.models.LaudoLaboratorial;
import com.us.Torres.models.SolicitacaoExame;
import com.us.Torres.models.laboratorio.LaudoRequest;
import com.us.Torres.service.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    @GetMapping("/exames")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','LABORATORISTA','TECNICO')")
    public ResponseEntity<List<SolicitacaoExame>> listarExames() {
        return ResponseEntity.ok(laboratorioService.listarExames());
    }

    @PostMapping("/exames")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','LABORATORISTA','TECNICO')")
    public ResponseEntity<SolicitacaoExame> solicitarExame(@RequestBody SolicitacaoExame exame) {
        return ResponseEntity.status(HttpStatus.CREATED).body(laboratorioService.solicitarExame(exame));
    }

    @GetMapping("/exames/{protocolo}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','LABORATORISTA','TECNICO')")
    public ResponseEntity<SolicitacaoExame> buscarExame(@PathVariable String protocolo) {
        return ResponseEntity.ok(laboratorioService.buscarPorProtocolo(protocolo));
    }

    @GetMapping("/exames/{protocolo}/laudo")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','LABORATORISTA','TECNICO')")
    public ResponseEntity<LaudoLaboratorial> obterLaudo(@PathVariable String protocolo) {
        return ResponseEntity.ok(laboratorioService.buscarLaudoPorProtocolo(protocolo));
    }

    @PatchMapping("/exames/{protocolo}/status")
    @PreAuthorize("hasAnyRole('ADMIN','LABORATORISTA','TECNICO','MEDICO','ENFERMEIRO')")
    public ResponseEntity<SolicitacaoExame> alterarStatus(@PathVariable String protocolo, @RequestParam SolicitacaoExame.StatusExame status) {
        return ResponseEntity.ok(laboratorioService.alterarStatus(protocolo, status));
    }

    @PatchMapping("/exames/{protocolo}/prioridade")
    @PreAuthorize("hasAnyRole('ADMIN','LABORATORISTA','TECNICO','MEDICO','ENFERMEIRO')")
    public ResponseEntity<SolicitacaoExame> alterarPrioridade(@PathVariable String protocolo, @RequestParam SolicitacaoExame.PrioridadeExame prioridade) {
        return ResponseEntity.ok(laboratorioService.alterarPrioridade(protocolo, prioridade));
    }

    @PostMapping("/exames/{protocolo}/laudo")
    @PreAuthorize("hasAnyRole('ADMIN','LABORATORISTA','TECNICO')")
    public ResponseEntity<LaudoLaboratorial> registrarLaudo(@PathVariable String protocolo, @RequestBody LaudoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(laboratorioService.registrarLaudo(protocolo, request));
    }
}
