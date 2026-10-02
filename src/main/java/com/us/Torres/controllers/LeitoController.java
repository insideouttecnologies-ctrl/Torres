package com.us.Torres.controllers;

import com.us.Torres.models.Leito;
import com.us.Torres.service.LeitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class LeitoController {

    private final LeitoService leitoService;

    @GetMapping("/leitos")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Leito>> listarLeitos() {
        return ResponseEntity.ok(leitoService.listarTodos());
    }

    @GetMapping("/leitos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Leito> buscarLeito(@PathVariable String id) {
        return ResponseEntity.ok(leitoService.buscarPorId(id));
    }

    @PostMapping("/leitos")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Leito> criarLeito(@RequestBody Leito leito) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leitoService.criar(leito));
    }

    @PutMapping("/leitos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Leito> atualizarLeito(@PathVariable String id, @RequestBody Leito leito) {
        return ResponseEntity.ok(leitoService.atualizar(id, leito));
    }

    @PatchMapping("/leitos/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Leito> alterarStatus(@PathVariable String id, @RequestParam Leito.StatusLeito status) {
        return ResponseEntity.ok(leitoService.alterarStatus(id, status));
    }
}
