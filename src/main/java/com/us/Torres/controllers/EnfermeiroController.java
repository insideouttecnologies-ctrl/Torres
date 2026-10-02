package com.us.Torres.controllers;

import com.us.Torres.models.Enfermeiro;
import com.us.Torres.service.EnfermeiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class EnfermeiroController {

    private final EnfermeiroService enfermeiroService;

    @GetMapping("/enfermeiros")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Enfermeiro>> listarEnfermeiros() {
        return ResponseEntity.ok(enfermeiroService.listarTodos());
    }

    @GetMapping("/enfermeiros/ativos")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Enfermeiro>> listarAtivos() {
        return ResponseEntity.ok(enfermeiroService.listarAtivos());
    }

    @GetMapping("/enfermeiros/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Enfermeiro> buscarEnfermeiro(@PathVariable String id) {
        return ResponseEntity.ok(enfermeiroService.buscarPorId(id));
    }

    @PostMapping("/enfermeiros")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> criarEnfermeiro(@RequestBody Enfermeiro enfermeiro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enfermeiroService.criar(enfermeiro));
    }

    @PutMapping("/enfermeiros/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> atualizarEnfermeiro(@PathVariable String id, @RequestBody Enfermeiro enfermeiro) {
        return ResponseEntity.ok(enfermeiroService.atualizar(id, enfermeiro));
    }

    @PatchMapping("/enfermeiros/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> alterarStatus(@PathVariable String id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(enfermeiroService.alterarStatus(id, ativo));
    }
}
