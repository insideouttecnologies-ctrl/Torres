package com.us.Torres.controllers;

import com.us.Torres.models.Consulta;
import com.us.Torres.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/consulta", "/v1/consulta"})
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Consulta>> listarConsultas() {
        return ResponseEntity.ok(consultaService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Consulta> buscarConsulta(@PathVariable String id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Consulta> criarConsulta(@RequestBody Consulta consulta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.criar(consulta));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Consulta> atualizarStatus(@PathVariable String id, @RequestParam Consulta.StatusConsulta status) {
        return ResponseEntity.ok(consultaService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable String id) {
        consultaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
