package com.us.Torres.controllers;

import com.us.Torres.models.Agendamento;
import com.us.Torres.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @GetMapping("/agendamentos")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Agendamento>> listarAgendamentos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    @GetMapping("/agendamentos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Agendamento> buscarAgendamento(@PathVariable String id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @PostMapping("/agendamentos")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Agendamento> agendar(@RequestBody Agendamento agendamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.agendar(agendamento));
    }

    @PatchMapping("/agendamentos/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Agendamento> atualizarStatus(@PathVariable String id, @RequestParam Agendamento.StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.atualizarStatus(id, status));
    }

    @DeleteMapping("/agendamentos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable String id) {
        agendamentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
