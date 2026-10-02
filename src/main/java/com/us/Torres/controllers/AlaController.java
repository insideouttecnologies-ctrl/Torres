package com.us.Torres.controllers;

import com.us.Torres.models.Ala;
import com.us.Torres.service.AlaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class AlaController {

    private final AlaService alaService;

    @GetMapping("/alas")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<List<Ala>> listarAla() {
        return ResponseEntity.ok(alaService.listarTodas());
    }

    @GetMapping("/alas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Ala> buscarAla(@PathVariable String id) {
        return ResponseEntity.ok(alaService.buscarPorId(id));
    }

    @PostMapping("/alas")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','RECEPCIONISTA')")
    public ResponseEntity<Ala> criarAla(@RequestBody Ala ala) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alaService.criar(ala));
    }

    @PutMapping("/alas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO')")
    public ResponseEntity<Ala> atualizarAla(@PathVariable String id, @RequestBody Ala ala) {
        return ResponseEntity.ok(alaService.atualizar(id, ala));
    }
}
