package com.us.Torres.controllers;

import com.us.Torres.models.Paciente;
import com.us.Torres.service.PacienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/paciente", "/api/v1/pacientes", "/v1/paciente", "/v1/pacientes"})
@AllArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Paciente> salvarPaciente(@RequestBody Paciente paciente) {
        return pacienteService.salvar(paciente);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Paciente>> obterPacientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String status) {
        return pacienteService.obterPacientes(nome, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Paciente> obterPacientePorId(@PathVariable String id) {
        return pacienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable String id, @RequestBody Paciente paciente) {
        return pacienteService.atualizar(id, paciente);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','RECEPCIONISTA')")
    public ResponseEntity<Void> excluirPaciente(@PathVariable String id) {
        return pacienteService.excluir(id);
    }
}
