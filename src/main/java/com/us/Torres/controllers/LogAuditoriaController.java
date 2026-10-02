package com.us.Torres.controllers;

import com.us.Torres.models.LogAuditoria;
import com.us.Torres.service.LogAuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/auditoria", "/v1/auditoria"})
@RequiredArgsConstructor
public class LogAuditoriaController {

    private final LogAuditoriaService logAuditoriaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogAuditoria>> listarLogs() {
        return ResponseEntity.ok(logAuditoriaService.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogAuditoria>> listarPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(logAuditoriaService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LogAuditoria> registrarLog(@RequestBody LogAuditoria logAuditoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logAuditoriaService.registrar(
                logAuditoria.getUsuarioId(),
                logAuditoria.getAcao(),
                logAuditoria.getModulo(),
                logAuditoria.getIpOrigem(),
                logAuditoria.getDetalhes()
        ));
    }
}
