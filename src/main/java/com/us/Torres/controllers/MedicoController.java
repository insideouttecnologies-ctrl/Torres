package com.us.Torres.controllers;

import com.us.Torres.models.Medico;
import com.us.Torres.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping({"/medicos", "/medico"})
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','RECEPCIONISTA','ENFERMEIRO')")
    public ResponseEntity<List<Medico>> listarMedicos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping({"/medicos/ativos", "/medico/ativos"})
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','RECEPCIONISTA','ENFERMEIRO')")
    public ResponseEntity<List<Medico>> listarMedicosAtivos() {
        return ResponseEntity.ok(medicoService.listarAtivos());
    }

    @GetMapping({"/medicos/{id}", "/medico/{id}"})
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','RECEPCIONISTA','ENFERMEIRO')")
    public ResponseEntity<Medico> buscarMedico(@PathVariable String id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @PostMapping({"/medicos", "/medico"})
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Medico> criarMedico(@RequestBody Medico medico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.criar(medico));
    }

    @PutMapping({"/medicos/{id}", "/medico/{id}"})
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Medico> atualizarMedico(@PathVariable String id, @RequestBody Medico medico) {
        return ResponseEntity.ok(medicoService.atualizar(id, medico));
    }

    @PatchMapping({"/medicos/{id}/status", "/medico/{id}/status"})
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Medico> alterarStatus(@PathVariable String id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(medicoService.alterarStatus(id, ativo));
    }
}
