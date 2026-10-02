package com.us.Torres.controllers;

import com.us.Torres.models.Medicamento;
import com.us.Torres.models.MovimentacaoEstoque;
import com.us.Torres.models.farmacia.EstoqueMovimentoRequest;
import com.us.Torres.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @GetMapping("/estoque")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<Medicamento>> listarEstoque() {
        return ResponseEntity.ok(estoqueService.listarEstoque());
    }

    @GetMapping("/estoque/alerta")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<Medicamento>> listarEstoqueBaixo() {
        return ResponseEntity.ok(estoqueService.listarEstoqueBaixo());
    }

    @PostMapping("/estoque/movimentacao")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO')")
    public ResponseEntity<MovimentacaoEstoque> registrarMovimentacao(@RequestBody EstoqueMovimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.registrarMovimentacao(request));
    }
}
