package com.us.Torres.controllers;

import com.us.Torres.models.Triagem;
import com.us.Torres.service.TriagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/triagem", "/v1/triagem"})
@RequiredArgsConstructor
public class TriagemController {

    private final TriagemService triagemService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENFERMEIRO','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<Triagem> registrarTriagem(@RequestBody Triagem triagem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(triagemService.salvarTriagem(triagem));
    }

    @GetMapping("/fila")
    @PreAuthorize("hasAnyRole('ADMIN','ENFERMEIRO','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<List<Triagem>> listarFila() {
        return ResponseEntity.ok(triagemService.listarFila());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','ENFERMEIRO','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<Triagem> atualizarStatus(@PathVariable String id, @RequestParam Triagem.StatusTriagem status) {
        return ResponseEntity.ok(triagemService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENFERMEIRO','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<Void> cancelarTriagem(@PathVariable String id) {
        triagemService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
