package com.us.Torres.controllers;

import com.us.Torres.models.Funcionario;
import com.us.Torres.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping("/funcionarios")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO','FARMACEUTICO','TECNICO')")
    public ResponseEntity<List<Funcionario>> listarFuncionarios() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    @GetMapping("/funcionarios/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO','FARMACEUTICO','TECNICO')")
    public ResponseEntity<Funcionario> buscarFuncionario(@PathVariable String id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    @GetMapping("/funcionarios/codigo/{codigoFuncionario}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO','FARMACEUTICO','TECNICO')")
    public ResponseEntity<Funcionario> buscarPorCodigo(@PathVariable String codigoFuncionario) {
        return ResponseEntity.ok(funcionarioService.buscarPorCodigo(codigoFuncionario));
    }

    @PostMapping("/funcionarios")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Funcionario> criarFuncionario(@RequestBody Funcionario funcionario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioService.criar(funcionario));
    }

    @PutMapping("/funcionarios/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Funcionario> atualizarFuncionario(@PathVariable String id, @RequestBody Funcionario funcionario) {
        return ResponseEntity.ok(funcionarioService.atualizar(id, funcionario));
    }

    @PatchMapping("/funcionarios/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Funcionario> alterarStatus(@PathVariable String id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(funcionarioService.alterarStatus(id, ativo));
    }
}
