package com.us.Torres.controllers;

import com.us.Torres.models.Medicamento;
import com.us.Torres.service.MedicamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/medicamentos", "/v1/medicamentos"})
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA')")
    public ResponseEntity<List<Medicamento>> listarMedicamentos() {
        return ResponseEntity.ok(medicamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA')")
    public ResponseEntity<Medicamento> buscarMedicamento(@PathVariable String id) {
        return ResponseEntity.ok(medicamentoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','ENFERMEIRO')")
    public ResponseEntity<Medicamento> criarMedicamento(@RequestBody Medicamento medicamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoService.criar(medicamento));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO')")
    public ResponseEntity<Medicamento> atualizarMedicamento(@PathVariable String id, @RequestBody Medicamento medicamento) {
        return ResponseEntity.ok(medicamentoService.atualizar(id, medicamento));
    }

    @PatchMapping("/{id}/estoque")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','ENFERMEIRO')")
    public ResponseEntity<Medicamento> atualizarEstoque(@PathVariable String id, @RequestParam Integer quantidade) {
        return ResponseEntity.ok(medicamentoService.atualizarEstoque(id, quantidade));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO')")
    public ResponseEntity<Void> removerMedicamento(@PathVariable String id) {
        medicamentoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
