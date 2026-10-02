package com.us.Torres.controllers;

import com.us.Torres.models.ConfiguracaoHospital;
import com.us.Torres.service.ConfiguracaoHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/v1/configuracao", "/v1/configuracao"})
@RequiredArgsConstructor
public class ConfiguracaoHospitalController {

    private final ConfiguracaoHospitalService configuracaoHospitalService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO','LABORATORISTA')")
    public ResponseEntity<ConfiguracaoHospital> buscarConfiguracao() {
        return ResponseEntity.ok(configuracaoHospitalService.obterConfiguracao());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfiguracaoHospital> atualizarConfiguracao(@RequestBody ConfiguracaoHospital configuracao) {
        return ResponseEntity.ok(configuracaoHospitalService.atualizar(configuracao));
    }
}
