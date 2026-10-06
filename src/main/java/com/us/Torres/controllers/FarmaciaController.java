package com.us.Torres.controllers;

import com.us.Torres.models.Receita;
import com.us.Torres.models.farmacia.DispensacaoRequest;
import com.us.Torres.models.Dispensacao;
import com.us.Torres.service.DispensacaoService;
import com.us.Torres.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/farmacia", "/v1/farmacia"})
@RequiredArgsConstructor
public class FarmaciaController {

    private final ReceitaService receitaService;
    private final DispensacaoService dispensacaoService;

    @GetMapping("/prescricoes")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','MEDICO','ENFERMEIRO')")
    public ResponseEntity<List<Receita>> listarPrescricoes(@RequestParam(required = false) String status) {
        if ("TODAS".equalsIgnoreCase(status) || "ALL".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(receitaService.listarTodas());
        }
        return ResponseEntity.ok(receitaService.listarPendentes());
    }

    @PostMapping("/prescricoes")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Receita> criarPrescricao(@RequestBody Receita receita) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(receitaService.criar(receita));
    }

    @PostMapping("/dispensar")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO')")
    public ResponseEntity<Dispensacao> dispensar(@RequestBody DispensacaoRequest request) {
        return ResponseEntity.ok(dispensacaoService.dispensar(request));
    }

    @GetMapping("/dispensacoes")
    @PreAuthorize("hasAnyRole('ADMIN','FARMACEUTICO','MEDICO','ENFERMEIRO')")
    public ResponseEntity<List<Dispensacao>> listarDispensacoes() {
        return ResponseEntity.ok(dispensacaoService.listarTodas());
    }
}
