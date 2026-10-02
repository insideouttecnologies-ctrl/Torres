package com.us.Torres.controllers;

import com.us.Torres.models.UnidadeHospitalar;
import com.us.Torres.service.UnidadeHospitalarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/unidades", "/v1/unidades"})
@RequiredArgsConstructor
public class UnidadeHospitalarController {

    private final UnidadeHospitalarService unidadeHospitalarService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO','LABORATORISTA')")
    public ResponseEntity<List<UnidadeHospitalar>> listarUnidades() {
        return ResponseEntity.ok(unidadeHospitalarService.listarTodos());
    }

    @GetMapping("/ativas")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO','LABORATORISTA')")
    public ResponseEntity<List<UnidadeHospitalar>> listarUnidadesAtivas() {
        return ResponseEntity.ok(unidadeHospitalarService.listarAtivas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO','LABORATORISTA')")
    public ResponseEntity<UnidadeHospitalar> buscarUnidade(@PathVariable String id) {
        return ResponseEntity.ok(unidadeHospitalarService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UnidadeHospitalar> criarUnidade(@RequestBody UnidadeHospitalar unidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeHospitalarService.criar(unidade));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UnidadeHospitalar> atualizarUnidade(@PathVariable String id, @RequestBody UnidadeHospitalar unidade) {
        return ResponseEntity.ok(unidadeHospitalarService.atualizar(id, unidade));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UnidadeHospitalar> alterarStatus(@PathVariable String id, @RequestParam boolean ativa) {
        return ResponseEntity.ok(unidadeHospitalarService.alterarStatus(id, ativa));
    }
}
