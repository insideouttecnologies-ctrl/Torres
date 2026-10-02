package com.us.Torres.controllers;

import com.us.Torres.service.DashboardResumo;
import com.us.Torres.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/dashboard", "/v1/dashboard"})
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping({"", "/", "/resumo"})
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO','FARMACEUTICO')")
    public ResponseEntity<DashboardResumo> obterResumo() {
        return ResponseEntity.ok(dashboardService.obterResumo());
    }

    @GetMapping("/kpis")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO','ENFERMEIRO','FARMACEUTICO')")
    public ResponseEntity<DashboardResumo> obterKpis() {
        return ResponseEntity.ok(dashboardService.obterResumo());
    }
}
